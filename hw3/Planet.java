public class Planet {
	// Planet class
	
	public double x;
	public double y;
	public double xVelocity;
	public double yVelocity;
	public double mass;
	public String img;

	public double xNetForce;
	public double yNetForce;
	public double xAccel;
	public double yAccel;

	private double radius;

	public static final double G = 6.67 * Math.pow(10, -11);

	public Planet(double xP, double yP, double xV, double yV, double m, String i, double r) {
		x = xP;
		y = yP;
		xVelocity = xV;
		yVelocity = yV;
		mass = m;
		img = i;
		radius = r;
	}

	public double getRadius() {
		return radius;
	}

	public double getMass() {
		return mass;
	}

	public double calcDistance(Planet p) {
		double deltaX = p.x - this.x;
		double deltaY = p.y - this.y;
		return Math.sqrt(deltaY * deltaY + deltaX * deltaX);
	}

	public double calcPairwiseForce(Planet p) {
		double r = this.calcDistance(p);
		return G * this.mass * p.mass / (r * r);
	}

	public double calcPairwiseForceX(Planet p) {
		double r = this.calcDistance(p);
		double f = this.calcPairwiseForce(p);
		double dx = p.x - this.x;
		return f * dx / r;
	}

	public double calcPairwiseForceY(Planet p) {
		double r = this.calcDistance(p);
		double f = this.calcPairwiseForce(p);
		double dy = p.y - this.y;
		return f * dy / r;
	}

	public void setNetForce(Planet[] ps) {
		xNetForce = 0;
		yNetForce = 0;
		for (Planet p: ps) {
			if (p != this) {
				xNetForce += calcPairwiseForceX(p);
				yNetForce += calcPairwiseForceY(p);
			}
		}
	}

	public void draw() {
		StdDraw.picture(x, y, "images/" + img);
	}

	public void update(double dt) {
		xAccel = xNetForce / mass;
		yAccel = yNetForce / mass;
		xVelocity += dt * xAccel;
		yVelocity += dt * yAccel;
		x += dt * xVelocity;
		y += dt * yVelocity;
	}
}