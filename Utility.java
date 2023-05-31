public class Utility{
    public static void color(String str){
        str = str.toLowerCase();
        if(str.equals("red"))getColorCode(1);
        else if(str.equals("black"))getColorCode(0);
        else if(str.equals(("green")))getColorCode(2);
        else if(str.equals("yellow"))getColorCode(3);
        else if(str.equals("blue"))getColorCode(4);
        else if(str.equals("magneta"))getColorCode(5);
        else if(str.equals("cyan"))getColorCode(6);
        else if(str.equals("white"))getColorCode(7);
        else getColorCode(8);

    }

    public static void getColorCode(int index){
        /**
         * Color codes are as follows :- 
         * 
            Black: \u001b[30m
            Red: \u001b[31m
            Green: \u001b[32m
            Yellow: \u001b[33m
            Blue: \u001b[34m
            Magenta: \u001b[35m
            Cyan: \u001b[36m
            White: \u001b[37m

            Reset: \u001b[0m

         */
        if(index == 0)System.out.println("\u001b[30m");
        else if(index == 1)System.out.println("\u001b[31m");
        else if(index == 2)System.out.println("\u001b[32m");
        else if(index == 3)System.out.println("\u001b[33m");
        else if(index == 4)System.out.println("\u001b[34m");
        else if(index == 5)System.out.println("\u001b[35m");
        else if(index == 6)System.out.println("\u001b[36m");
        else if(index == 7)System.out.println("\u001b[37m");
        else if(index == 8)System.out.println("\u001b[0m");





    }
    
}