/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.client.util.baker.model;

import org.lwjgl.util.glu.Quadric;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by <Arekkuusu> on 06/07/2017.
 * It's distributed as part of Improbable plot machine.
 */
public class Cube extends Quadric {

	public void draw() {
		glBegin(GL_QUADS);
		//Front
		glVertex3f(-0.5F, -0.5F, -0.5F);
		glVertex3f(0.5F, -0.5F, -0.5F);
		glVertex3f(0.5F, 0.5F, -0.5F);
		glVertex3f(-0.5F, 0.5F, -0.5F);
		//Back
		glVertex3f(0.5F, -0.5F, 0.5F);
		glVertex3f(-0.5F, -0.5F, 0.5F);
		glVertex3f(-0.5F, 0.5F, 0.5F);
		glVertex3f(0.5F, 0.5F, 0.5F);
		//Right
		glVertex3f(0.5F, -0.5F, -0.5F);
		glVertex3f(0.5F, -0.5F, 0.5F);
		glVertex3f(0.5F, 0.5F, 0.5F);
		glVertex3f(0.5F, 0.5F, -0.5F);
		//Left
		glVertex3f(-0.5F, -0.5F, 0.5F);
		glVertex3f(-0.5F, -0.5F, -0.5F);
		glVertex3f(-0.5F, 0.5F, -0.5F);
		glVertex3f(-0.5F, 0.5F, 0.5F);
		//Top
		glVertex3f(-0.5F, 0.5F, -0.5F);
		glVertex3f(0.5F, 0.5F, -0.5F);
		glVertex3f(0.5F, 0.5F, 0.5F);
		glVertex3f(-0.5F, 0.5F, 0.5F);
		//Bottom
		glVertex3f(-0.5F, -0.5F, 0.5F);
		glVertex3f(0.5F, -0.5F, 0.5F);
		glVertex3f(0.5F, -0.5F, -0.5F);
		glVertex3f(-0.5F, -0.5F, -0.5F);
		glEnd();
	}
}
