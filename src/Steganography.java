
import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

/**
 *
 * I did the extra credit for overloading hideText()/revealText()
 */
public class Steganography {

    /* Clear the lower (rightmost) two bits in a pixel. */
    public static void clearLow(Pixel p) {
        Color c = p.getColor();
        int r = (c.getRed() / 4 * 4);
        int b = (c.getBlue() / 4 * 4);
        int g = (c.getGreen() / 4 * 4);
        p.setColor(new Color(r, g, b));
    }

    // this is the extra credit overloaded method. :D
    public static void clearLow(Pixel p, int digitsCleared) {
        Color c = p.getColor();
        int numb = (int) Math.pow(2, digitsCleared);
        int r = (c.getRed() / numb * numb);
        int b = (c.getBlue() / numb * numb);
        int g = (c.getGreen() / numb * numb);
        p.setColor(new Color(r, g, b));
    }

    /* Method to test clearLow on all pixels in a Picture */
    public static Picture testClearLow(Picture pic) {
        Picture p = new Picture(pic);
        Pixel[][] pixels = p.getPixels2D();
        for (Pixel[] pArr : pixels) {
            for (Pixel px : pArr) {
                clearLow(px);
            }
        }
        return p;
    }

    /* Replace the lowest 2 bits of each colot value in p to the highest 2 bits of color value in c */
    public static void setLow(Pixel p, Color c) {
        clearLow(p);
        Color pc = p.getColor();
        int r = (pc.getRed() + c.getRed() / 64);
        int g = (pc.getGreen() + c.getGreen() / 64);
        int b = (pc.getBlue() + c.getBlue() / 64);
        p.setColor(new Color(r, g, b));

    }

    /* Method to test setLow on all pixels in a Picture */
    public static Picture testSetLow(Picture pic, Color col) {
        Picture p = new Picture(pic);
        Pixel[][] pixels = p.getPixels2D();
        for (Pixel[] pArr : pixels) {
            for (Pixel px : pArr) {
                setLow(px, col);
            }
        }
        return p;
    }

    /**
     * Determines whether secret can be hidden in source, which is true if
     * source and secret are the same dimensions.
     *
     * @param source is not null
     * @param secret is not null
     * @return true if secret can be hidden in source, false otherwise.
     */
    public static boolean canHide(Picture source, Picture secret) {
        return source.getWidth() == secret.getWidth() && source.getHeight() == secret.getHeight();
    }

    /**
     * Creates a new Picture with data from secret hidden in data from source
     *
     * @param source is not null
     * @param secret is not null
     * @return combined Picture with secret hidden in source precondition:
     * source is same width and height as secret
     */
    public static Picture hidePicture(Picture source, Picture secret) {
        return hidePicture(source, secret, 0,0);
    }
//NOT DONE YET FINISH THIS LATER 

    public static Picture hidePicture(Picture source, Picture secret, int startRow, int startColumn) {
        Picture p = new Picture(source);
        Pixel[][] copy = p.getPixels2D();
        Pixel[][] sources = source.getPixels2D();
        Pixel[][] secrets = secret.getPixels2D();

        for (int r = startRow; r < secrets.length + startRow; r++) {
            for (int j = startColumn; j < secrets[0].length + startColumn; j++) {
                clearLow(sources[r][j]);
                int red = sources[r][j].getRed() + secrets[r - startRow][j - startColumn].getRed() / 64;
                int blue = sources[r][j].getBlue() + secrets[r - startRow][j - startColumn].getBlue() / 64;
                int green = sources[r][j].getGreen() + secrets[r - startRow][j - startColumn].getGreen() / 64;
                copy[r][j].setColor(new Color(red, green, blue));

            }
        }
        return p;
    }
    public static Picture hidePicture(Picture source, Picture secret, boolean yes) {
        int row =  (int)(source.getWidth()- secret.getWidth() * Math.random());
        int column =  (int)(source.getHeight()- secret.getHeight() * Math.random());
        return hidePicture(source,secret,row,column);


    }

    /*
    *check to see if pic1 and pic2 match
     */
    public static boolean isSame(Pixel p1, Pixel p2) {
        return (p1.getRed() == p2.getRed() && p1.getBlue() == p2.getBlue() && p1.getGreen() == p2.getGreen());
    }

    public static boolean isSame(Picture pic1, Picture pic2) {
        if (pic1.getHeight() != pic2.getHeight() || pic1.getWidth() != pic2.getWidth()) {
            return false;
        }
        Pixel[][] pixels2 = pic2.getPixels2D();
        Pixel[][] pixels1 = pic1.getPixels2D();
        for (int r = 0; r < pixels1.length; r++) {
            for (int c = 0; c < pixels2[0].length; c++) {
                Pixel p1 = pixels1[r][c];
                Pixel p2 = pixels2[r][c];
                if (!isSame(p1, p2)) {
                    return false;
                }
            }
        }
        return true;

    }

    public static ArrayList<Point> findDifferences(Picture pic1, Picture pic2) {
        ArrayList<Point> differences = new ArrayList();
        //check every pixel and isSame of them if they are not the same add it to differences

        Pixel[][] pixels2 = pic2.getPixels2D();
        Pixel[][] pixels1 = pic1.getPixels2D();
        if (pixels1.length != pixels2.length || pixels1[0].length != pixels2[0].length) {
            return differences;
        }
        for (int r = 0; r < pixels1.length; r++) {
            for (int c = 0; c < pixels1[0].length; c++) {
                Pixel p1 = pixels1[r][c];
                Pixel p2 = pixels2[r][c];
                if (!isSame(p1, p2)) {
                    differences.add(new Point(r, c));
                }
            }
        }
        return differences;

    }

    public static ArrayList<Integer> encodeString(String s) {
        String temp = s.toUpperCase();
        ArrayList<Integer> myList = new ArrayList<Integer>();
        int num = -1;
        for (int i = 0; i < s.length(); i++) {
            if (temp.charAt(i) == ' ') {
                num = 27;
            } else {
                num = temp.charAt(i) - 64; //auto casts
            }
            myList.add(num);

        }
        myList.add(0);
        return myList;
    }

    /**
     * Returns the string represented by the codes arraylist. 1-26 = A-Z, 27 =
     * space
     *
     * @param codes encoded string
     * @return decoded string
     */
    /* Sets the highest two bits of each pixel’s colors 
 * to the lowest two bits of each pixel’s colors 
     */
    private static String decodeString(ArrayList<Integer> codes) {
        String decoded = "";
        for (int i = 0; i < codes.size(); i++) {
            if(codes.get(i) == 27) {
                decoded = decoded + " ";
            }
            else if(codes.get(i) == 0) {
                return decoded;
            }
            else{
                char letter = (char)(codes.get(i) + 64);
                decoded = decoded + letter;
            }
        }
        return decoded;
    }
//    private static int[] getBitPairs(int num) {
//        int[] pairs = new int[3];
//        pairs[0]= (num/16) % 2;//the mod 2 just cleans up if the number is too large
//        pairs[1] = (num/4) % 4;
//        pairs[2] = num % 4;
//        return pairs;
//    }
//when you accidently write the method you have already written :( at least I realized it was the wrong way. 
    public static Picture revealPicture(Picture hidden) {
        Picture copy = new Picture(hidden);
        Pixel[][] pixels = copy.getPixels2D();
        Pixel[][] source = hidden.getPixels2D();
        for (int r = 0; r < pixels.length; r++) {
            for (int c = 0; c < pixels[0].length; c++) {
                Color col = source[r][c].getColor();
                /* To be Implemented */
                int red = col.getRed() % 4 * 64;// mod 4 to get the last 2 digits then move it over 6 digits
                int green = col.getGreen() % 4 * 64;
                int blue = col.getBlue() % 4 * 64;
                pixels[r][c].setColor(new Color(red, green, blue));

            }
        }
        return copy;
    }

    private static int[] getBitPairs(int num) {
        int[] pairs = new int[3];
        for (int i = 0; i < pairs.length; i++) {
            pairs[i] = num % 4;
            num /= 4;
        }
        return pairs;
    }
    private static int getNumFromPairs(int[] nums){
        int number = 0;
        for(int i = 0; i < nums.length; i++ ) {
            number += nums[i];
            number *= 4;
            
        }
        return number;
    }

    public static Picture hideText(Picture source, String s) {
        Picture combined = new Picture(source);
        Pixel[][] copy = combined.getPixels2D();
        ArrayList<Integer> nums = encodeString(s);
        int i = 0;
        for (int r = 0; r < source.getHeight(); r++) {
            for (int c = 0; c < source.getWidth(); c++) {
                Pixel p = combined.getPixel(r, c);
                if(i > nums.size() -1) {
                    return combined;
                }
                int[] pairs = getBitPairs(nums.get(i));
                clearLow(p);
                copy[r][c].setColor(new Color(p.getRed() + pairs[0], p.getGreen() + pairs[1], p.getBlue() + pairs[2]));        
                i++;
                
            }
        }
        return combined;
        //Create the list of integers representing the encoded message
        //Traverse the pixels in the image. 
        //For each pixel where a character will be hidden
        //Get the bit pairs of the integer representation of the character
        //Add the bit pairs to the R, G, B values of the pixel
    }
//overloaded    
    public static Picture hideText(Picture source, String s, int row, int column) {
        Picture combined = new Picture(source);
        Pixel[][] copy = combined.getPixels2D();
        ArrayList<Integer> nums = encodeString(s);
        int i = 0;
        for (int r = row; r < source.getHeight(); r++) {
            for (int c = column; c < source.getWidth(); c++) {
                Pixel p = combined.getPixel(r, c);
                if(i > nums.size() -1) {
                    return combined;
                }
                int[] pairs = getBitPairs(nums.get(i));
                clearLow(p);
                copy[r][c].setColor(new Color(p.getRed() + pairs[0], p.getGreen() + pairs[1], p.getBlue() + pairs[2]));        
                i++;
                
            }
        }
        return combined;
        //Create the list of integers representing the encoded message
        //Traverse the pixels in the image. 
        //For each pixel where a character will be hidden
        //Get the bit pairs of the integer representation of the character
        //Add the bit pairs to the R, G, B values of the pixel
    }
    
    public static String revealText(Picture source) {
        Picture combined = new Picture(source);
        Pixel[][] copy = combined.getPixels2D();
        ArrayList<Integer> codes = new ArrayList<Integer>();
        int i = 0;
        for (int r = 0; r < source.getHeight(); r++) {
            for (int c = 0; c < source.getWidth(); c++) {
                Pixel p = copy[r][c];
                int letter = p.getRed()%4 + p.getGreen()%4 * 4 + p.getBlue()%4 * 16;// I toyed with the multiplier(because they were switched to get it to work. 
                //System.out.println(letter);
                if (letter == 0) {
                    return decodeString(codes);
                }
                codes.add(letter);
                
                         
            }
        }
        return decodeString(codes);
    }
    //overloaded
    public static String revealText(Picture source, int row, int column) {
        Picture combined = new Picture(source);
        Pixel[][] copy = combined.getPixels2D();
        ArrayList<Integer> codes = new ArrayList<Integer>();
        int i = 0;
        for (int r = row; r < source.getHeight(); r++) {
            for (int c = column; c < source.getWidth(); c++) {
                Pixel p = copy[r][c];
                int letter = p.getRed()%4 + p.getGreen()%4 * 4 + p.getBlue()%4 * 16;// I toyed with the multiplier(because they were switched to get it to work. 
                //System.out.println(letter);
                if (letter == 0) {
                    return decodeString(codes);
                }
                codes.add(letter);
                
                         
            }
        }
        return decodeString(codes);
    }


    public static Picture showDifferentArea(Picture pic, ArrayList<Point> points) {
        Picture copy = new Picture(pic);
        Pixel[][] pixels = copy.getPixels2D();
        
        int left = points.get(0).x;
        int right = points.get(0).x;
        int top = points.get(0).y;
        int bottom = points.get(0).y;
        for (int i = 1; i < points.size(); i++) {
            Point current = points.get(i);
            if (left > current.x) {
                left = current.x;
            }
            if (right < current.x){
                right = current.x;
            }
            
            if (top > current.y) {
                top = current.y;
            }
            if(bottom < current.y) {
                bottom = current.y;
            }

        }
        Point topLeft = new Point(left, top);//I had already written it for having top and bottom points so I just left it like that
        Point bottomRight = new Point(right, bottom);
        System.out.println("top left is" + topLeft.x + " " + topLeft.y);
        System.out.println("bot right is" + bottomRight.x + " " + bottomRight.y);
        for (int x = topLeft.x; x < bottomRight.x; x++) {
            pixels[x][topLeft.y].setColor(Color.red);
            pixels[x][bottomRight.y].setColor(Color.red);
        }
        for (int y = topLeft.y; y < bottomRight.y; y++) {
            pixels[topLeft.x][y].setColor(Color.red);
            pixels[bottomRight.x][y].setColor(Color.red);

        }
        return copy;
    }
    public static Picture resize(Picture source, double scale) {
        double factor = Math.pow(scale, 0.5);
        Picture resized = new Picture((int)(source.getHeight() * factor), (int)(source.getWidth() * factor));
        for (int r = 0; r < resized.getHeight(); r++)
            for (int c = 0; c < resized.getWidth(); c++) {
                Pixel sourcePx = source.getPixel((int) (c/factor), (int) (r/factor));
                Pixel resizedPx = resized.getPixel(c,r);
                resizedPx.setColor(sourcePx.getColor());
            }
        return resized;
    }
    public static void clearLow(Pixel px, char colorValue) {
        if(colorValue == 'r') {
            px.setRed(px.getRed() / 4  *4);
        } else if(colorValue == 'g') {
            px.setGreen(px.getGreen()/4  *4);
        } else {
            px.setBlue(px.getBlue()/4 * 4);
        }
    }
    public static void setLow(Picture p, int[] start, char colorValue, Color c) {
        int[] colors = {c.getRed(), c.getGreen(), c.getBlue()};
        for(int i = 0; i < 3;i++) {
            Pixel px = p.getPixel(start[0] + i, start[1]);
            clearLow(px, colorValue);
            if(colorValue == 'r') {
            px.setRed(px.getRed() + colors[i] / 64);
        } else if(colorValue == 'g') {
            px.setGreen(px.getGreen() + colors[i] / 64);
        } else {
            px.setBlue(px.getBlue() + colors[i] / 64);
        }
        }
    }
    
    public static Picture fancyHidePicture(Picture source, Picture secret) {
        char[] colors = {'r','g','b'};
        Picture combined = new Picture(source);
        for(int r = 0; r < secret.getHeight();r++) {
            for(int c = 0; c<secret.getWidth(); c++) {
                Pixel secretPx = secret.getPixel(c,r);
                if(c < secret.getWidth() / 3 * 3) {
                    int[] start = {c / 3 * 3, r};
                    setLow(combined, start, colors[c % 3], secretPx.getColor());
                } else {
                    setLow(combined.getPixel(c, r), secretPx.getColor());
                }
                
                
                
            }
        }
        return combined;          
    }
    public static Color revealColor(Picture p, int[] start, char colorVal) {
        int[] rgb = new int[3];
        for(int i = 0; i < 3; i++) {
            if(colorVal == 'r') {
                rgb[i] = p.getPixel(start[0] + i, start[1]).getRed() %4  * 64;
            }
            else if(colorVal == 'g') {
                rgb[i] = p.getPixel(start[0] + i, start[1]).getGreen() %4  * 64;
            } else {
                rgb[i] = p.getPixel(start[0] + i, start[1]).getBlue() %4  * 64;
            }
        }
        return new Color(rgb[0], rgb[1], rgb[2]);
    }
    public static Picture fancyRevealPicture(Picture hidden, int width, int height) {
        char[] colors = {'r','g','b'};
        Picture revealed = new Picture(height, width);
        for (int r = 0; r < revealed.getHeight(); r++) {
            for (int c = 0; c < revealed.getWidth(); c++) {
                Pixel revealedPx = revealed.getPixel(c,r);
                if(c < revealed.getWidth() /3 * 3) {
                    int[] start = {c/3 * 3, r};
                    revealedPx.setColor(revealColor(hidden,start, colors[c % 3]));
                    
                } else {
                    Pixel sourcePx = hidden.getPixel(c,r);
                    int red =  sourcePx.getRed() %4 * 64;
                    int green =  sourcePx.getGreen() %4 * 64;
                    int blue =  sourcePx.getBlue() %4 * 64;
                    revealedPx.setColor(new Color(red, green, blue));
                }
            }
        }
        return revealed;    
                
    }

    public static void main(String[] args) {
//        System.out.println(encodeString("hello world"));
//        
        Picture swan = new Picture("swan.jpg");
//        String text = revealText(hideText(swan, "HELLO WORLD"));
//        System.out.println(text);
//        String text2 = revealText(hideText(swan, "HELLO WORLD NUMBER TWO", 5,5),5,5);
//        System.out.println(text2);
//        Picture swan2 = new Picture("swan.jpg");
//        System.out.println("Swan and swan2 are the same: " + isSame(swan, swan2));
//        swan = testClearLow(swan);
//        System.out.println("Swan and swan2 are the same (after clearLow run on swan): " + isSame(swan, swan2));
//        System.out.println("swan in swan2 " + canHide(swan, swan2));
//
        Picture beach2 = new Picture("beach.jpg");
        Picture fancy = fancyHidePicture(beach2, swan);
        fancy.explore();
        resize(fancy, 3).explore();
        resize(fancy, 0.5).explore();
        //fancyRevealPicture(fancy, swan.getWidth(), swan.getHeight()).explore();
        
//        System.out.println("swan in beach" + canHide(swan, beach2));
//        System.out.println("beach in swan" + canHide(beach2, swan));
//        Picture gorge = new Picture("gorge.jpg");
//        if (canHide(swan, gorge)) {
//            hidePicture(swan, gorge).explore();
//            hidePicture(gorge, swan).explore();
//            revealPicture(hidePicture(gorge, swan)).explore();
//            revealPicture(hidePicture(swan, gorge)).explore();
//        }

//        beach2.explore();
//        Picture copy2 = testSetLow(beach2, Color.PINK);
//        revealPicture(copy2).explore();//this will reveal the color we put over the whole thing(pink) 
//      
//        copy2.explore();
//        Picture beach = new Picture("beach.jpg");
//        //beach.explore();
//        Picture beachCopy = testClearLow(beach);
//        //copy.explore();
//        Picture arch = new Picture("arch.jpg");
//        System.out.println(canHide(beach, beachCopy));
//        System.out.println(canHide(beach, arch));
//        Picture arch = new Picture("arch.jpg");
//        Picture koala = new Picture("koala.jpg");
//        Picture robot1 = new Picture("robot.jpg");
//        ArrayList<Point> pointList = findDifferences(arch, arch);
//        System.out.println("PointList after comparing two identical spictures has a size of " + pointList.size());
//        pointList = findDifferences(arch, koala);
//        System.out.println("PointList after comparing two different sized pictures has a size of " + pointList.size());
//        Picture arch2 = hidePicture(arch, robot1, 65, 102);
//        pointList = findDifferences(arch, arch2);
//        System.out.println("Pointlist after hiding a picture has a size of " + pointList.size());
//        arch.show();
//        arch2.show();
//
//        Picture hall = new Picture("femaleLionAndHall.jpg");
//        Picture robot2 = new Picture("robot.jpg");
//        Picture flower2 = new Picture("flower1.jpg");
//// hide pictures 
//        Picture hall2 = hidePicture(hall, robot2, 50, 300);
//        Picture hall3 = hidePicture(hall2, flower2, 115, 275);
//        hall3.explore();
//        if (!isSame(hall, hall3)) {
//            Picture hall4 = showDifferentArea(hall, findDifferences(hall, hall3));
//            hall4.show();
//            Picture unhiddenHall3 = revealPicture(hall3);
//            unhiddenHall3.show();
//
//        }
    }

}
