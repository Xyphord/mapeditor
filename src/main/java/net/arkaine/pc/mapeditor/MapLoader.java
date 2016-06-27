package net.arkaine.pc.mapeditor;

import javax.swing.*;
import java.io.Serializable;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: omarcou
 * Date: 22/10/13
 * Time: 14:36
 * To change this template use File | Settings | File Templates.
 */
public class MapLoader implements Serializable {

    private int typeDeVue = 0; //  0 = iso3D : 1 = Dungeon crawler : 2 = shoot em up : 3 = canonier

    //X Est en Ouest
    private int tailleX = 0;
    //Y Nord en Sud
    private int tailleY = 0;
    //Z Hauteur
    private int tailleZ = 0;


    private Map<Integer, ImageIcon> sols = new HashMap<Integer, ImageIcon>();
    private Map<Integer, ImageIcon> murs = new HashMap<Integer, ImageIcon>();

    private Case[][][] carte;

    public int getTailleX() {
        return tailleX;
    }

    public void setTailleX(int tailleX) {
        this.tailleX = tailleX;
    }

    public int getTailleY() {
        return tailleY;
    }

    public void setTailleY(int tailleY) {
        this.tailleY = tailleY;
    }

    public int getTailleZ() {
        return tailleZ;
    }

    public void setTailleZ(int tailleZ) {
        this.tailleZ = tailleZ;
    }


    public MapLoader(int x, int y, int z){
        this.carte = new Case[tailleX][tailleY][tailleZ];
    }

    public MapLoader(String sauvegarde, int typeDeVue){
        this.typeDeVue = typeDeVue;
        load(sauvegarde);
        switch (this.typeDeVue){
            case 0:
                this.sols = Utils.loadImages("solsIso");
                this.murs = Utils.loadImages("mursIso");
                break;
            case 1:
                this.sols = Utils.loadImages("solsShoot");
                this.murs = Utils.loadImages("mursShoot");
                break;
            case 2:
                this.sols = Utils.loadImages("solsDM");
                this.murs = Utils.loadImages("mursDM");
                break;
            case 3:
                this.sols = Utils.loadImages("solsCun");
                this.murs = Utils.loadImages("mursGun");
                break;
        }
    }

    /**
     * format fichier carte:
     * x.y.z;
     * 15_45_15_2_10_2_8|453_545_33_2_5_69_14|...|...;
     *
     * @param sauvegarde
     * @return
     */
    public boolean load(String sauvegarde){
        String data = Utils.openFile(Paths.get(sauvegarde));
        data = data.replaceAll("[\r\n ]+", "");
//        data = data.replace(" ","");
        if(data == null || data == "")
            return false;
        String[] resultat = data.split(";");
        if (resultat.length != 2) //deux secteurs dans le fichier : la taille des carte et leur données
            return false;

        String[] tailles = resultat[0].split("_"); //recuperation des dimensions
        if (tailles.length != 3)
            return false;
        tailleX = Integer.valueOf(tailles[0]);
        tailleY = Integer.valueOf(tailles[1]);
        tailleZ = Integer.valueOf(tailles[2]);

        carte = new Case[tailleX][tailleY][tailleZ];
        //on boucle sur les trois axes
        String[] carteBrut = resultat[1].split("\\|");
        int index = 0;
        for (int z=0;z<tailleZ;z++){
            for (int y=0;y<tailleY;y++){
                for (int x=0;x<tailleX;x++){
                    try{
                        carte[x][y][z] = new Case(carteBrut[index]);
                    }catch (ArrayIndexOutOfBoundsException e){
                        return false;
                    }
                    index++;
                }
            }
        }
        return true;
    }

    public boolean isSol(int coordZ){
        boolean isMurs = (coordZ%2 > 0);
        return !isMurs;
    }

    public boolean isMur(int coordZ){
        boolean isMurs = (coordZ%2 > 0);
        return !isMurs;
    }

    private int decalY(int yy, int xx, int newWidth) {
        switch (this.typeDeVue){
            case 0:
                return (int) (((newWidth*0.18f)*yy)+((newWidth*0.18f)*xx));
            case 1:
                return newWidth*yy;
            case 2:
                return 0;
            case 3:
                return 0;
        }
        return 0;
    }

    private int decalX(int yy, int xx, int newWidth) {
        switch (this.typeDeVue){
            case 0:
                return ((newWidth / 2) * xx) - ((newWidth / 2) * yy);
            case 1:
                return newWidth*xx;
            case 2:
                return 0;
            case 3:
                return 0;
        }
        return 0;
    }

}