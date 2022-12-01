package me.emprzedd.artifactframework;

public enum Rarity{
    COMMON,
    RARE,
    UNIQUE,
    DIVINE,
    ADMIN,
    LORE;

    //text formatting https://codepen.io/0biwan/pen/ggVemP
    public static String formatText(Rarity rarity, String text) {
        String formattedName = "&4&lERROR"+text;
        if(rarity== COMMON) {//?????
            formattedName= "&8&l//&7&o"+text+"&8&l//";
        }
        else if(rarity== RARE) {//green
            formattedName= "&a&o«"+text+"»";
            //&a&o«Magic Sand»
        }
        else if(rarity== UNIQUE) {//blue
            formattedName = "&9&l&o&n"+text.charAt(0)+"&b&9"+text.substring(1);//i dont care for this error checking, your dumb if you wanted a blank text and the jar should throw an outofbounds exeception you trash.
        }
        else if(rarity== DIVINE) {
            //&5&l&k0&r&5&l║ T H E  ·  D R A G O N  ·  E G G ║&k0
            //&e&o]&r&l&e║&r &l&5P&r &l&dO P&r &o&e·&r &l&5R&r &l&dO C K&r &l&e║&r&o&e&o]
            //"&e&k]&r&l&e║&r&l&5" + FIRST_WORD_LETTER + "&l&dO"+ FIRST_LEFTOVER  ?SPACE=("&o&e·&r") + " &l&e║&r&o&e&k]"
            StringBuilder sb = new StringBuilder();
            String[] words = text.split(" ");

            sb.append("&e&k]&r&l&e║&r&l&5");


            for(int wordPos=0; wordPos<words.length;wordPos++) {


                sb.append("&5&l").append(words[wordPos].charAt(0)).append("&d&o");

                for(int charPos=1; charPos < words[wordPos].length();charPos++) {
                    sb.append(" ").append(words[wordPos].charAt(charPos));
                }

                //doesnt add space after last word
                if(wordPos != words.length-1)
                    sb.append("&r  &o&e·  &r");
            }
            sb.append("&r&l&e║&r&e&k]");

            formattedName = sb.toString();
        }
        else if(rarity== ADMIN) {//gold
            formattedName = "&c&l&o⚡&6&o&l"+text+"&r&c&l&o⚡";
        }
        else if(rarity == LORE){
            formattedName = "&e&o"+text;
        }

        return formattedName;
    }
}

