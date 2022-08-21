package com.nexttech.entities;

import java.awt.image.BufferedImage;

import com.nexttech.main.Game;
import com.nexttech.world.World;

public class Enemy extends Entity{

	private double speed = 1;
	
	public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
	}
	
	public void update() {
		if((int)x < Game.player.getX() && World.isFree((int)(x+speed), this.getY())) {
			x+=speed;
		} else if((int)x > Game.player.getX() && World.isFree((int)(x-speed), this.getY())) {
			x-=speed;
		}
		
		if((int)y < Game.player.getY() && World.isFree(this.getX(), (int)(y+speed))) {
			y+=speed;
		} else if((int)y > Game.player.getY() && World.isFree(this.getX(), (int)(y-speed))) {
			y-=speed;
		}
	}

}