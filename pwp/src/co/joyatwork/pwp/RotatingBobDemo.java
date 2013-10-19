/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package co.joyatwork.pwp;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class RotatingBobDemo extends InputAdapter implements ApplicationListener {

	public static class MyGestureListener implements GestureListener {

		@Override
		public boolean touchDown(float x, float y, int pointer, int button) {
			Gdx.app.log(TAG, "touchDown()");
			return false;
		}

		@Override
		public boolean tap(float x, float y, int count, int button) {
			Gdx.app.log(TAG, "tap()");
			return false;
		}

		@Override
		public boolean longPress(float x, float y) {
			Gdx.app.log(TAG, "longPress()");
			return false;
		}

		@Override
		public boolean fling(float velocityX, float velocityY, int button) {
			Gdx.app.log(TAG, "fling()");
			return false;
		}

		@Override
		public boolean pan(float x, float y, float deltaX, float deltaY) {
			Gdx.app.log(TAG, "pan()"
					+ " x: " + x
					+ " y: " + y
					+ " delta-x: " + deltaX
					+ " delta-y: " + deltaY
					);
			return false;
		}

		@Override
		public boolean panStop(float x, float y, int pointer, int button) {
			Gdx.app.log(TAG, "panStop()"
					+ " x: " + x
					+ " y: " + y
					);
			return false;
		}

		@Override
		public boolean zoom(float initialDistance, float distance) {
			Gdx.app.log(TAG, "zoom()");
			return false;
		}

		@Override
		public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
				Vector2 pointer1, Vector2 pointer2) {
			Gdx.app.log(TAG, "pinch()");
			return false;
		}
		
	}
	private static final String TAG = "RotatingBob";
	ShaderProgram shader;
	Mesh mesh;
	Texture texture;
	Matrix4 matrix = new Matrix4();

	@Override
	public void create () {
		String vertexShader = "attribute vec4 a_position;    \n" + "attribute vec4 a_color;\n" + "attribute vec2 a_texCoord0;\n"
			+ "uniform mat4 u_worldView;\n" + "varying vec4 v_color;" + "varying vec2 v_texCoords;"
			+ "void main()                  \n" + "{                            \n" + "   v_color = vec4(1, 1, 1, 1); \n"
			+ "   v_texCoords = a_texCoord0; \n" + "   gl_Position =  u_worldView * a_position;  \n"
			+ "}                            \n";
		String fragmentShader = "#ifdef GL_ES\n" + "precision mediump float;\n" + "#endif\n" + "varying vec4 v_color;\n"
			+ "varying vec2 v_texCoords;\n" + "uniform sampler2D u_texture;\n" + "void main()                                  \n"
			+ "{                                            \n" + "  gl_FragColor = v_color * texture2D(u_texture, v_texCoords);\n"
			+ "}";

		shader = new ShaderProgram(vertexShader, fragmentShader);
		if (shader.isCompiled() == false) {
			Gdx.app.log(TAG, shader.getLog());
			Gdx.app.exit();
		}

		mesh = new Mesh(true, 4, 6, VertexAttribute.Position(), VertexAttribute.ColorUnpacked(), VertexAttribute.TexCoords(0));
		mesh.setVertices(new float[] {-0.5f, -0.5f, 0, 1, 1, 1, 1, 0, 1, 0.5f, -0.5f, 0, 1, 1, 1, 1, 1, 1, 0.5f, 0.5f, 0, 1, 1, 1,
			1, 1, 0, -0.5f, 0.5f, 0, 1, 1, 1, 1, 0, 0});
		mesh.setIndices(new short[] {0, 1, 2, 2, 3, 0});
		texture = new Texture(Gdx.files.internal("data/bobrgb888-32x32.png"));
		
		//Gdx.input.setInputProcessor(this);
		Gdx.input.setInputProcessor(new GestureDetector(new MyGestureListener()));
	}

	Vector3 axis = new Vector3(0, 0, 1);
	float angle = 0;

	@Override
	public void render () {
		angle += Gdx.graphics.getDeltaTime() * 45;
		matrix.setToRotation(axis, angle);

		Gdx.gl20.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl20.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl20.glEnable(GL20.GL_TEXTURE_2D);
		Gdx.gl20.glEnable(GL10.GL_BLEND);
		Gdx.gl20.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		texture.bind();
		shader.begin();
		shader.setUniformMatrix("u_worldView", matrix);
		shader.setUniformi("u_texture", 0);
		mesh.render(shader, GL10.GL_TRIANGLES);
		shader.end();
	}

	@Override
	public void dispose () {
		mesh.dispose();
		texture.dispose();
		shader.dispose();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		Gdx.app.log(TAG, "pause()");
	}

	@Override
	public void resume() {
		Gdx.app.log(TAG, "resume()");
	}
	
	@Override
	public boolean touchDown (int screenX, int screenY, int pointer, int button) {
		Gdx.app.log(TAG, "touchDown()");
		return false;
	}

	@Override
	public boolean touchUp (int screenX, int screenY, int pointer, int button) {
		Gdx.app.log(TAG, "touchUp()");
		return false;
	}

	@Override
	public boolean touchDragged (int screenX, int screenY, int pointer) {
		Gdx.app.log(TAG, "touchDragged()"
				+ " x: " + screenX
				+ " y: " + screenY
				);
		return false;
	}

}
