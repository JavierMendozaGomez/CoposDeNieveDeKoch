package model;

public class Segmento {
	
	private PV2D initPoint;
	
	private double dir;
	private double dist;
	
	public Segmento(double x, double y, double dir, double dist){
		this.initPoint = new PV2D(x, y);
		this.dir = dir;
		this.dist = dist;
	}
	
	public PV2D getInitPoint(){
		return this.initPoint;
	}
	
	public double getDir(){
		return this.dir;
	}
	
	public double getDist(){
		return this.dist;
	}
}
