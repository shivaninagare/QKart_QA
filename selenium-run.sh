#!/bin/bash
# Stop the existing the zalenium docker container if its runnning
sudo docker stop zalenium

# Directories to add to .gitignore, ex: images and remove images to free memory
find . -type f \( -iname '.jpg' -o -iname '.jpeg' -o -iname '*.png' \) -exec dirname {} \; | while read dir; do
    # Remove the leading './' from the directory path
    dir=${dir#./}
    if ! grep -q "^$dir\$" .gitignore; then
        echo "$dir" >> .gitignore
        echo "Added $dir to .gitignore."
    fi
    rm -rf $dir/*.jpg $dir/*.jpeg $dir/*.png
    echo "removing old screenshots"
done


# Start the zalenium docker container in 8082 port
sudo /usr/bin/docker run -d --rm -i --name zalenium -p 8082:4444 \
      -v /var/run/docker.sock:/var/run/docker.sock \
      -v /tmp/videos:/home/seluser/videos \
      --privileged dosel/zalenium start --videoRecordingEnabled false  --desiredContainers 1

ip="$(curl -s http://checkip.amazonaws.com/)"
url="http://$ip:8082/vnc/host/172.17.0.3/port/50000/?nginx=&path=proxy/172.17.0.3:50000/websockify&view_only=false"

for i in {0..31}
do
    count=$(sudo docker ps | grep zalenium_ |  wc -l)
    if [[ "$count" = "1" ]]; then
        break
    fi

    if [[ "$i" = "30" ]]; then
        echo "$(tput setaf 1) Error starting zalenium container. Please contact Crio Support"
        exit 1
    fi
    
    sleep 1
done

sleep 4
echo ""
echo ""
echo $url
exit 0

