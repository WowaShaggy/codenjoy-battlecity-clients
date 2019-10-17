package clientlib;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static clientlib.Action.AFTER_TURN;
import static clientlib.Elements.*;


public class SampleSolver extends Solver {


    public List<Point> getPlayerTankCoordinates(){
            List<Point> playerTank = getCoordinates(TANK_DOWN, TANK_UP, TANK_LEFT, TANK_RIGHT);
            if(playerTank.size() == 0){
                playerTank.add(new Point(0,0));
            }
            return playerTank;
        }


    public List<Point> getOtherPlayersTanks(){
        List<Point> otherPlayers = getCoordinates(OTHER_TANK_DOWN, OTHER_TANK_UP, OTHER_TANK_LEFT, OTHER_TANK_RIGHT);
        return otherPlayers;
    }

    public List<Point> getBotsTanks(){
        List<Point> bots = getCoordinates(AI_TANK_DOWN, AI_TANK_UP, AI_TANK_LEFT, AI_TANK_RIGHT);
        return bots;
    }

    public List<Point> getBullets(){
        List<Point> bullets = getCoordinates(BULLET);
        return bullets;
    }


    public List<Point> getConstructions(){
        List<Point> constructions = getCoordinates(CONSTRUCTION);
        return constructions;
    }

    public List<Point> getDestroyedConstructions(){
        List<Point> constructions = getCoordinates(CONSTRUCTION_DESTROYED_DOWN,
                CONSTRUCTION_DESTROYED_UP,
                CONSTRUCTION_DESTROYED_LEFT,
                CONSTRUCTION_DESTROYED_RIGHT,
                CONSTRUCTION_DESTROYED,

                CONSTRUCTION_DESTROYED_DOWN_TWICE,
                CONSTRUCTION_DESTROYED_UP_TWICE,
                CONSTRUCTION_DESTROYED_LEFT_TWICE,
                CONSTRUCTION_DESTROYED_RIGHT_TWICE,

                CONSTRUCTION_DESTROYED_LEFT_RIGHT,
                CONSTRUCTION_DESTROYED_UP_DOWN,

                CONSTRUCTION_DESTROYED_UP_LEFT,
                CONSTRUCTION_DESTROYED_RIGHT_UP,
                CONSTRUCTION_DESTROYED_DOWN_LEFT,
                CONSTRUCTION_DESTROYED_DOWN_RIGHT);
        return constructions;
    }

    public List<Point> getWalls(){
        List<Point> walls = getCoordinates(BATTLE_WALL);
        return walls;
    }

    public List<Point> getBarriers(){
        List<Point> barriers = new ArrayList<>();
        barriers.addAll(getWalls());
        barriers.addAll(getConstructions());
        barriers.addAll(getDestroyedConstructions());
        barriers.addAll(getOtherPlayersTanks());
        barriers.addAll(getBotsTanks());
        return barriers;
    }

    public boolean isNear(int x, int y, Elements el){
        return isAt(x+1,y,el) ||
                isAt(x-1,y, el) ||
                isAt(x, y-1, el) ||
                isAt(x, y+1, el);
    }

    public boolean isBarrierAt(int x, int y){
        return getBarriers().contains(new Point(x,y));
    }

    public boolean isAnyOfAt(int x, int y, Elements... elements){
        boolean result = false;
        for (Elements el : elements){
            result = isAt(x, y, el);
            if(result) break;
        }
        return result;
    }

    public boolean isAt(int x, int y, Elements element){
        if(isOutOfBounds(x, y)){
            return false;
        } else{
            return field[x][y] == element;
        }
    }

    public int countNear(int x, int y, Elements element){
        int counter = 0;
        if(isAt(x+1, y, element)) counter++;
        if(isAt(x-1, y, element)) counter++;
        if(isAt(x, y+1, element)) counter++;
        if(isAt(x, y-1, element)) counter++;
        return counter;
    }

    public boolean isOutOfBounds(int x, int y){
        return x >= field.length || y >= field.length || x < 0 || y < 0;
    }


    public List<Point> getCoordinates(Elements... searchElements){
        Set<Elements> searchSetElements = new HashSet<>(Arrays.asList(searchElements));
        List<Point> elementsCoordinates = new ArrayList<>();

        for (int y = 0; y < field.length; y++) {
            for (int x = 0; x < field.length; x++) {
                if (searchSetElements.contains(field[x][y])) {
                    elementsCoordinates.add(new Point(x,y));
                }
            }
        }
        return elementsCoordinates;
    }



    @Override
    public String move() throws java.lang.NullPointerException {        
                                                                                                                         // My tank's location
        List<Point> cor = getPlayerTankCoordinates();   
        System.out.println("x=" +cor.get(0).getX() + " y=" +cor.get(0).getY());
        
                                                                                                                         // The nearest enemy tank
        if (isAnyOfAt(cor.get(0).getX(),cor.get(0).getY()-1, OTHER_TANK_UP,OTHER_TANK_RIGHT,OTHER_TANK_DOWN,OTHER_TANK_LEFT,
                                                                 AI_TANK_UP, AI_TANK_RIGHT, AI_TANK_DOWN, AI_TANK_LEFT)){
            System.out.println("Up is tank");
            return up(AFTER_TURN);
        }
        if (isAnyOfAt(cor.get(0).getX()+1, cor.get(0).getY(), OTHER_TANK_UP,OTHER_TANK_RIGHT,OTHER_TANK_DOWN,OTHER_TANK_LEFT,
                                                                   AI_TANK_UP, AI_TANK_RIGHT, AI_TANK_DOWN, AI_TANK_LEFT)){
            System.out.println("right is tank");
            return right(AFTER_TURN);
        }
        if (isAnyOfAt(cor.get(0).getX(),cor.get(0).getY()+1, OTHER_TANK_UP,OTHER_TANK_RIGHT,OTHER_TANK_DOWN,OTHER_TANK_LEFT,
                                                                 AI_TANK_UP, AI_TANK_RIGHT, AI_TANK_DOWN, AI_TANK_LEFT)){
            System.out.println("down is tank");
            return down(AFTER_TURN);
        }
        if (isAnyOfAt(cor.get(0).getX()-1,cor.get(0).getY(), OTHER_TANK_UP,OTHER_TANK_RIGHT,OTHER_TANK_DOWN,OTHER_TANK_LEFT,
                                                                AI_TANK_UP, AI_TANK_RIGHT, AI_TANK_DOWN, AI_TANK_LEFT)){
            System.out.println("left is tank");
            return left(AFTER_TURN);
        }
                                                                                                                        // Blocked directions
       List<String> dir = new ArrayList<>();
       dir.add("up");dir.add("right");dir.add("down");dir.add("left");
       
           if(isBarrierAt(cor.get(0).getX(),cor.get(0).getY()-1) || isAt(cor.get(0).getX(),cor.get(0).getY()-1, BATTLE_WALL) || isAt(cor.get(0).getX(),cor.get(0).getY()-1, BULLET)){
              dir.set(0, null);
             // if (isAnyOfAt(cor.get(0).getX(),cor.get(0).getY()-1, CONSTRUCTION_DESTROYED, BANG)){
             //     System.out.println("way will be clear in a second");
             //     dir.set(0, "up");
             // }
           }
           if(isBarrierAt(cor.get(0).getX()+1,cor.get(0).getY()) || isAt(cor.get(0).getX()+1,cor.get(0).getY(), BATTLE_WALL) || isAt(cor.get(0).getX()+1,cor.get(0).getY(), BULLET)){
               dir.set(1, null);
              // if (isAnyOfAt(cor.get(0).getX()+1,cor.get(0).getY(), CONSTRUCTION_DESTROYED, BANG)){
              //     System.out.println("way will be clear in a second");
              //     dir.set(1, "right");
              // }
           }
           if(isBarrierAt(cor.get(0).getX(),cor.get(0).getY()+1) || isAt(cor.get(0).getX(),cor.get(0).getY()+1, BATTLE_WALL) || isAt(cor.get(0).getX(),cor.get(0).getY()+1, BULLET)){
               dir.set(2, null);
              // if (isAnyOfAt(cor.get(0).getX(),cor.get(0).getY()+1, CONSTRUCTION_DESTROYED, BANG)){
              //     System.out.println("way will be clear in a second");
              //     dir.set(2, "down");
              // }
           }
           if(isBarrierAt(cor.get(0).getX()-1,cor.get(0).getY() )|| isAt(cor.get(0).getX()-1,cor.get(0).getY(), BATTLE_WALL) || isAt(cor.get(0).getX()-1,cor.get(0).getY(), BULLET)){
               dir.set(3, null);
              // if (isAnyOfAt(cor.get(0).getX()-1,cor.get(0).getY(), CONSTRUCTION_DESTROYED, BANG)){
              //     System.out.println("way will be clear in a second");
              //     dir.set(3, "left");
              // }
           }
           
        System.out.println("list: " + dir.get(0)+ "  " + dir.get(1)+ "  " + dir.get(2)+ "  " + dir.get(3));
                                                                                                                        // Enemies are in the distance  
        for ( Point i: getOtherPlayersTanks()) {
            for (int ii = 1; ii < 3; ii++) {
                if (i.getY() == cor.get(0).getY()) {
                    if (i.getX() + ii == cor.get(0).getX() && dir.get(3) != null) {
                        System.out.println("enemy is left in " + ii);
                        return left(AFTER_TURN);
                    }
                    if (i.getX() - ii == cor.get(0).getX() && dir.get(1) != null) {
                        System.out.println("enemy is right in " + ii);
                        return right(AFTER_TURN);
                    }
                }
                if (i.getX() == cor.get(0).getX()) {
                    if (i.getY() + ii == cor.get(0).getY() && dir.get(0) != null) {
                        System.out.println("enemy is up in " + ii);
                        return up(AFTER_TURN);
                    }
                    if (i.getY() - ii == cor.get(0).getY() && dir.get(2) != null) {
                        System.out.println("enemy is down in " + ii);
                        return down(AFTER_TURN);
                    }
                }
            }
        }
                                                                                                                        // Enemies are far away
        for ( Point i: getOtherPlayersTanks()) {
            if (i.getY() == cor.get(0).getY() && Math.abs(i.getX() - cor.get(0).getX()) <= 12) {
                if (i.getX() < cor.get(0).getX() && dir.get(3) != null) {
                    System.out.println("enemy is left" + " x=" + i.getX() + " y=" + i.getY());
                    return left(AFTER_TURN);
                }
                if (i.getX() > cor.get(0).getX() && dir.get(1) != null) {
                    System.out.println("enemy is right" + " x=" + i.getX() + " y=" + i.getY());
                    return right(AFTER_TURN);
                }
            }
            if (i.getX() == cor.get(0).getX() && Math.abs(i.getY() - cor.get(0).getY()) <= 5) {
                if (i.getY() < cor.get(0).getY() && dir.get(0) != null) {
                    System.out.println("enemy is up" + " x=" + i.getX() + " y=" + i.getY());
                    return up(AFTER_TURN);
                }
                if (i.getY() > cor.get(0).getY() && dir.get(2) != null) {
                    System.out.println("enemy is down" + " x=" + i.getX() + " y=" + i.getY());
                    return down(AFTER_TURN);
                }
            }
        }
                                                                                                                        // one way or another
        int a; 
        boolean ok;
        do {
            ok = true;
            a = ThreadLocalRandom.current().nextInt(0, dir.size());
            System.out.println("a = " + a);
            if (dir.get(a) == null)
            {
                ok = false;
                dir.remove(a);
                System.out.println("size became " + dir.size());
            }
        }while(ok == false);
        System.out.println("f.size is " + dir.size() +" and f.a = " + a);
        
        switch (dir.get(a)){                                 
            case "up":
                return up();
            case "right":
                return right();
            case "down":
                return down();
            case "left":
                return left();
            default: return down(AFTER_TURN);
        }
        

    }
    
}

