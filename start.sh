#!/bin/bash 

# echo "installing required packages"
# echo "---------------------------------------------------------------"
# sudo apt install graphviz
# sudo apt install graphicsmagick-imagemagick-compat
# echo "package installation complete"
# echo "---------------------------------------------------------------"
# set -e

# bpmn_arr=$(find "." -type f -name "*.bpmn")

# totalFile=${#bpmn_arr[@]}
# # echo "${totalFile}"
# a=1
# for file in ${totalFile}
# do
#  echo $a+" "+$file
# done


# echo "${bpmn_arr}"

# shopt -s globstar
# shopt -s dotglob nullglob

# dirs=( ./**/*.bpmn )          # glob the names
# # dirs=( "${dirs[@]%/*}" ) 

# declare -A seen
# for dirpath in "${dirs[@]}"; do
#     seen["$dirpath"]=''
# done

# dirs=( "${!seen[@]}" ) 
sudo apt install dmenu
sudo apt-get install rofi
rm *.class
javac Split.java
allfiles="$(find . -iname "*.bpmn")"
# echo "${allfiles}"
# echo ${#allfiles[@]}


while (true)
do
    

    choice=$(printf "${allfiles}" \
        | cut -d '/' -f3- \
        | sed 's/.bpmn/ /g' \
        | sort -g \
        | rofi -show  window -dmenu -l 30 -p 'Select BPMN File: ') || exit 1
    # echo $choice
    if [ "$choice" ]; then
        fileName=$(printf '%s\n' "./BPMN/${choice}" | sed 's/ /.bpmn/g')
        java Split ${fileName} 
        dot -Tpng  petrinet.dot > petrinet.png
        dot -Tpng  bpmn.dot > bpmn.png
        echo "opening BPMN Model and Petri Net Model"
        echo "---------------------------------------------------------------"
        bpmn_file=" $(echo $fileName | sed  's/bpmn/png/g')"
        display $bpmn_file & 
        display petrinet.png  \
        & display bpmn.png
    else
        echo "Program terminated." && exit 0
    fi

    # read -n1 c && printf "\nYou Pressed: %s\n" "$c"
    # this command will stop until the key is pressed
    read -n1 
    
done 2>/dev/null


# echo "${choice}"


# while(true);
# do
#     index=1
#     while [ $index -le ${#dirs[@]} ];
#     do
#         echo $index ${dirs[index-1]}
#         ((index++))
#     done
#     echo "Enter the file number you want to run";


#     read filenumber
#     if [[ $filenumber -le ${#dirs[@]} && $filenumber -ge 1 ]]
#     then
#         path_to_bpmnfile=${dirs[$filenumber-1]}
#         echo "the chosen file is "${path_to_bpmnfile};
#         java Split ${path_to_bpmnfile} 
#         dot -Tpng  petrinet.dot > petrinet.png
#         echo "opening BPMN Model and Petri Net Model"
#         echo "---------------------------------------------------------------"
#         my_str=${path_to_bpmnfile}   
#         echo "the actual path is $my_str"   
#         my_arr=${my_str%.*}  
   
#         a=${my_arr}
#         b='.png'
#         c="${a}${b}"
#         echo "new file " "${c}"
#         display $c & display petrinet.png
        
        
#     fi

# done
# # printf '%s\n' "${dirs[@]}"
# # totalFile=${#dirs[@]}
# # echo "${totalFile}"

# # echo "${png_arr}"
# # java Split
# echo "opening BPMN Model"
# echo "---------------------------------------------------------------"
# # dot -Tsvg bpmn.dot > bpmn.svg
# # dot -Tsvg petrinet.dot > petrinet.svg

# # display petrinet.svg
# # # display bpmn.svg & 

