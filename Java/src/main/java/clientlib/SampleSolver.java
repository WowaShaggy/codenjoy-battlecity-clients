package clientlib;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static clientlib.Action.AFTER_TURN;
import static clientlib.Action.BEFORE_TURN;
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
    public String move() {        
                                                                    // Координаты нашего танка
        List<Point> cor = getPlayerTankCoordinates();   
        System.out.println("x=" +cor.get(0).getX() + " y=" +cor.get(0).getY());
        
        // Проверка наличие рядом 
        if (isAnyOfAt(cor.get(0).getX(),cor.get(0).getY()+1, OTHER_TANK_UP,OTHER_TANK_RIGHT,OTHER_TANK_DOWN,OTHER_TANK_LEFT)){
            System.out.println("Up is tank");
            return up(AFTER_TURN);
        }
        if (isAnyOfAt(cor.get(0).getX()+1,cor.get(0).getY(), OTHER_TANK_UP,OTHER_TANK_RIGHT,OTHER_TANK_DOWN,OTHER_TANK_LEFT)){
            System.out.println("right is tank");
            return right(AFTER_TURN);
        }
        if (isAnyOfAt(cor.get(0).getX(),cor.get(0).getY()-1, OTHER_TANK_UP,OTHER_TANK_RIGHT,OTHER_TANK_DOWN,OTHER_TANK_LEFT)){
            System.out.println("down is tank");
            return down(AFTER_TURN);
        }
        if (isAnyOfAt(cor.get(0).getX()-1,cor.get(0).getY(), OTHER_TANK_UP,OTHER_TANK_RIGHT,OTHER_TANK_DOWN,OTHER_TANK_LEFT)){
            System.out.println("left is tank");
            return left(AFTER_TURN);
        }                             


                                                                // барьеры рядом
       List<Integer> dir = new ArrayList<>();

       dir.add(0);dir.add(1);dir.add(2);dir.add(3);

           if(isBarrierAt(cor.get(0).getX(),cor.get(0).getY()+1) || isAt(cor.get(0).getX(),cor.get(0).getY()+1, BATTLE_WALL)){
              dir.set(0, null);
           }
           if(isBarrierAt(cor.get(0).getX()+1,cor.get(0).getY()) || isAt(cor.get(0).getX()+1,cor.get(0).getY(), BATTLE_WALL)){
               dir.set(1, null);
           }
           if(isBarrierAt(cor.get(0).getX(),cor.get(0).getY()-1) || isAt(cor.get(0).getX(),cor.get(0).getY()-1, BATTLE_WALL)){
               dir.set(2, null);
           }
           if(isBarrierAt(cor.get(0).getX()-1,cor.get(0).getY() )|| isAt(cor.get(0).getX()-1,cor.get(0).getY(), BATTLE_WALL)){
               dir.set(3, null);
           }


        int a;
        do {
            System.out.println("list " + dir.get(0)+ "  " + dir.get(1)+ "  " + dir.get(2)+ "  " + dir.get(3));
            a = ThreadLocalRandom.current().nextInt(0, 4);
            System.out.println("a = " + a);
        }while(dir.get(a) == null);
        System.out.println("final a position = " + dir.get(a));
        switch (a){                                 
            case 0:
                return up(AFTER_TURN);
            case 1:
                return right(AFTER_TURN);
            case 2:
                return down(AFTER_TURN);
            case 3:
                return left(AFTER_TURN);
            default: return down(BEFORE_TURN);
        }
    }
}

