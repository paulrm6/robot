package attempt1;

import java.math.BigDecimal;

/**
 * Created by Paul on 24/10/2015.
 */
public class TheRobot {

    private BigDecimal interval = new BigDecimal("40");
    private BigDecimal x;
    private BigDecimal y;
    private BigDecimal z;

    private BigDecimal dx;
    private BigDecimal dy;
    private BigDecimal dz;
    private int[][] coord;
    private int t = 1;



    public TheRobot(int[][] coord) {

        this.x = new BigDecimal(coord[0][0]);
        this.y = new BigDecimal(coord[0][1]);
        this.z = new BigDecimal(coord[0][2]);
        this.coord = coord;
        setDifferences();
    }

    public void setDifferences(){
        setDx(newDifference(getX(), getCoord()[getT()][0]));
        setDy(newDifference(getY(), getCoord()[getT()][1]));
        setDz(newDifference(getZ(), getCoord()[getT()][2]));
    }

    public BigDecimal getX() {
        return x;
    }

    public void setX(BigDecimal x) {
        this.x = x;
    }

    public BigDecimal getY() {
        return y;
    }

    public void setY(BigDecimal y) {
        this.y = y;
    }

    public BigDecimal getZ() {
        return z;
    }

    public int[][] getCoord() {
        return coord;
    }

    public void setCoord(int[][] coord) {
        this.coord = coord;
    }

    public BigDecimal getDx() {
        return dx;
    }

    public void setDx(BigDecimal dx) {
        this.dx = dx;
    }

    public BigDecimal getDy() {
        return dy;
    }

    public void setDy(BigDecimal dy) {
        this.dy = dy;
    }

    public BigDecimal getDz() {
        return dz;
    }

    public void setDz(BigDecimal dz) {
        this.dz = dz;
    }

    public void setZ(BigDecimal z) {
        this.z = z;
    }

    public int getT() {
        return t;
    }

    public void setT(int t) {
        this.t = t;
    }

    public BigDecimal getInterval() {
        return interval;
    }

    public void setInterval(BigDecimal interval) {
        this.interval = interval;
    }

    private BigDecimal newDifference(BigDecimal bigDecimal, int integer) {
        return ((new BigDecimal(integer)).subtract(bigDecimal)).divide(interval);
    }
}
