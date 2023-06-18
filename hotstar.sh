#!/bin/bash 
cnt=1;
chrome(){
    google-chrome --new-window --incognito  "https://www.hotstar.com/in/shows/final-australia-vs-india-day-1/1540023545/watch?filters=content_type%3Dsport_live"
}
# PID=10101010101

while [[ $cnt -le 2 ]];
do
# open google chrome and start one more process in background sleep it for 240 seconds, start one more google chrome tab kill the previous one and continue this
# if [[ `expr $cnt%2` -eq 1 ]];then
#     sleep 10
#     google-chrome -newwindow --incognito "https://www.hotstar.com/in/shows/final-australia-vs-india-day-1/1540023545/watch?filters=content_type%3Dsport_live" & (PID=$!;
#     sleep 280 ; 
#     google-chrome -newwindow --incognito "https://www.hotstar.com/in/shows/final-australia-vs-india-day-1/1540023545/watch?filters=content_type%3Dsport_live" ;
#     kill $PID;)
# else
#     sleep 280
#     PID=$!
#     google-chrome -newwindow --incognito "https://www.hotstar.com/in/shows/final-australia-vs-india-day-1/1540023545/watch?filters=content_type%3Dsport_live" & (PID=$!;
#     sleep 280 ; 
#     google-chrome -newwindow --incognito "https://www.hotstar.com/in/shows/final-australia-vs-india-day-1/1540023545/watch?filters=content_type%3Dsport_live" ;
#     kill $PID;
#     )
# cnt=$(( $cnt + 1 ))
# fi


    (gnome-terminal -- sh -c  "google-chrome --new-window --incognito  "https://www.hotstar.com/in/shows/final-australia-vs-india-day-1/1540023545/watch?filters=content_type%3Dsport_live"") & (
    pgrep -lP $!)

done