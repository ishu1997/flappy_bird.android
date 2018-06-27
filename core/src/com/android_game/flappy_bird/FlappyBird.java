package com.android_game.flappy_bird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.graphics.Color;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture bird;

	Texture bottomtube , toptube;

	Circle birdcircle;
	Rectangle[] toprectangle ;
	Rectangle[]	bottomrectangle;
	BitmapFont font;


	int score=0;
	int scoringtube=0;

	float birdheight;
	float velocity;
	int gamestate=0;
	float gap=400;

	float maxTubeoffset; //maxx of the randomization the position of the tube
	 //this will decide the random coordinates
	Random randomgenerator;

	float tubevelocity=4;
	int numberoftubes =4;
	float[] tubex  = new float[numberoftubes];
	float[] tubeoffset = new float[4];
	float distancebetweentubes;


	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		bird = new Texture("source.gif");
		bottomtube = new Texture("bottomtube.png");
		toptube = new Texture("toptube.png");
		maxTubeoffset = Gdx.graphics.getHeight()/2-gap/2-100;

		birdheight = Gdx.graphics.getHeight()/2-50;
		randomgenerator = new Random();
		distancebetweentubes=Gdx.graphics.getWidth()* 3/4;
		toprectangle = new Rectangle[numberoftubes];
		bottomrectangle  = new Rectangle[numberoftubes];

		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10);

		for(int i=0;i<numberoftubes;i++){

			tubeoffset[i] =(randomgenerator.nextFloat()-0.5f)*(Gdx.graphics.getHeight()-gap-200);
            tubex[i] = Gdx.graphics.getWidth()/2 - toptube.getWidth()/2 + Gdx.graphics.getWidth()+i*distancebetweentubes;


			toprectangle[i] = new Rectangle();
			bottomrectangle[i] = new Rectangle();




		}


	}

	@Override
	public void render () {

		birdcircle = new Circle();
		batch.begin();
		batch.draw(background,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());


		if(gamestate != 0){

			if(Gdx.input.justTouched()){
				velocity=-20;
			}

			for(int i=0;i<numberoftubes;i++) {
				if(tubex[i] < -toptube.getWidth()){
					tubeoffset[i] =(randomgenerator.nextFloat()-0.5f)*(Gdx.graphics.getHeight()-gap-200);
					tubex[i] = numberoftubes * distancebetweentubes;
				}

				else {
					tubex[i] -= tubevelocity;
					if(tubex[scoringtube]< Gdx.graphics.getWidth()/2){
						score++;
						Gdx.app.log("score" , String.valueOf(score));
						if(scoringtube < numberoftubes -1){
							scoringtube++;
							}
							else{
							scoringtube=0;
						}

					}
				}
				batch.draw(toptube, tubex[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeoffset[i]);
				batch.draw(bottomtube, tubex[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomtube.getHeight() + tubeoffset[i]);
				//setting up shape for top and bottom tubes
				toprectangle[i].set( tubex[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeoffset[i],toptube.getWidth(),toptube.getHeight());
				bottomrectangle[i].set(tubex[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomtube.getHeight() + tubeoffset[i],bottomtube.getWidth() , bottomtube.getHeight());
			}
			if(birdheight > 0 || velocity < 0){
				velocity++;
				birdheight-=velocity;
			}
		}

		else{
			if(Gdx.input.justTouched()){
				gamestate=1;
			}
		}


		batch.draw(bird,Gdx.graphics.getWidth()/2-50,birdheight, 100 , 100);
		font.draw(batch,String.valueOf(score) , 50 , 150);
		batch.end();

		birdcircle.set(Gdx.graphics.getWidth()/2 , birdheight+50 , 50);


		for(int i=0;i<numberoftubes;i++){
			if(Intersector.overlaps(birdcircle,toprectangle[i]) || Intersector.overlaps(birdcircle,bottomrectangle[i])){
				Gdx.app.log("collision" , "occur");
			}
		}




	}
	
	@Override
	public void dispose () {
		batch.dispose();
		background.dispose();
	}
}
