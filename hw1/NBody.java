public class NBody {
	public static void main(String[] args) {
		double T = Double.parseDouble(args[0]);
		double dt = Double.parseDouble(args[1]);
		String filename = args[2];
		
		In reader = new In(filename);

		int numPlanets = reader.readInt();
		double universeRadius = reader.readDouble();

		Planet[] planets = new Planet[numPlanets];
		for (int i = 0; i < numPlanets; i++) {
			planets[i] = getPlanet(reader);
		}

		StdDraw.setScale(-universeRadius, universeRadius);
		StdDraw.picture(0, 0, "images/starfield.jpg");
		for (Planet p: planets) {
			p.draw();
		}

		double time = 0;
		while (time < T) {
			for (Planet p: planets) {
				p.setNetForce(planets);
			}

			for (Planet p: planets) {
				p.update(dt);
			}

			StdDraw.picture(0, 0, "images/starfield.jpg");
			for (Planet p: planets) {
				p.draw();
			}

			StdDraw.show(10);
			time += dt;
		}

		StdOut.printf("%d\n", numPlanets);
		StdOut.printf("%.2e\n", universeRadius);
		for (int i = 0; i < numPlanets; i++) {
			Planet p = planets[i];
    		StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n", 
    			p.x, p.y, p.xVelocity, p.yVelocity, p.mass, p.img);
    	}
	}

	public static Planet getPlanet(Object obj) {
		In reader = (In) obj;

		double x = reader.readDouble();
		double y = reader.readDouble();
		double xVelocity = reader.readDouble();
		double yVelocity = reader.readDouble();
		double mass = reader.readDouble();
		String img = reader.readString();
		return new Planet(x, y, xVelocity, yVelocity, mass, img);
	}
}