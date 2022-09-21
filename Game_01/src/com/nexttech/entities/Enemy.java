package com.nexttech.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.nexttech.main.Game;
import com.nexttech.world.Camera;
import com.nexttech.world.World;

public class Enemy extends Entity{

	private double speed = 0.6;
	private int frames = 0, maxFrames = 16, index = 0, maxIndex = 1;
	private BufferedImage[] enemy;
	private int life = 10;
	//Definições da mascara de colisao
	private int maskx = 3, masky = 8, maskw = 10, maskh = 6;
	private boolean isDamaged = false;
	private int damageFrames = 0;
	
	public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		
		enemy = new BufferedImage[2];
		for(int i = 0; i <= maxIndex; i++) {
			enemy[i] = Game.spritesheet.getSprite(112 + (i*16), 16, 16, 16);
		}
		
	}
	
	public void update() {
		/*maskx = 8;
		masky = 8;
		maskw = 5;
		maskh = 5;
		*/
		
		if (isCollidingWithPlayer() == false) {
			if((int)x < Game.player.getX() 
					&& World.isFree((int)(x+speed), this.getY(), z)
					&& !isColliding((int)(x+speed), this.getY())) {
				x+=speed;
			} else if((int)x > Game.player.getX() 
					&& World.isFree((int)(x-speed), this.getY(), z)
					&& !isColliding((int)(x-speed), this.getY())) {
				x-=speed;
			}
			
			if((int)y < Game.player.getY() 
					&& World.isFree(this.getX(), (int)(y+speed), z)
					&& !isColliding(this.getX(), (int)(y+speed))) {
				y+=speed;
			} else if((int)y > Game.player.getY() 
					&& World.isFree(this.getX(), (int)(y-speed), z)
					&& !isColliding(this.getX(), (int)(y-speed))) {
				y-=speed;
			}
		} else if(Game.player.z == this.z){
			// Estamos colidindo
			if(Game.rand.nextInt(100) < 10) {
				//Sound.hurtEffect.play();
				Game.player.setLife(Game.player.getLife() - Game.rand.nextInt(3));
				Game.player.isDamaged = true;
				//System.out.println("Vida: " + Game.player.life);
			}
		}
		
		frames++;
		if(frames == maxFrames) {
			frames = 0;
			index++;
			if(index > maxIndex) {
				index = 0;
			}
		}
		
		collidingBullet();
		
		if(life <= 0) {
			destroySelf();
			return;
		}
		
		if(isDamaged) {
			damageFrames++;
			if(damageFrames == 6) {
				damageFrames = 0;
				isDamaged = false;
			}
		}
		
	}
	
	public void render(Graphics g) {
		if(!isDamaged) {
			g.drawImage(enemy[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
		} else {
			if(index == 0) {
				g.drawImage(Entity.ENEMY_FEEDBACK, this.getX() - Camera.x, this.getY() - Camera.y, null);
			} else if(index == 1) {
				g.drawImage(Entity.ENEMY_FEEDBACK2, this.getX() - Camera.x, this.getY() - Camera.y, null);
			}
		}
		//Renderiza mascara atual
		//g.setColor(Color.BLUE);
		//g.fillRect(getX() + maskx - Camera.x, getY() + masky - Camera.y, maskw, maskh);
	}
	
	public void destroySelf() {
		Game.enemies.remove(this);
		Game.entities.remove(this);
	}
	
	public void collidingBullet() {
		for(int i = 0; i < Game.shoots.size(); i++) {
			Entity e = Game.shoots.get(i);
			if(e instanceof Shoot) {
				if(Entity.isColliding(this, e)) {
					isDamaged = true;
					life -= 2;
					Game.shoots.remove(i);
					return;					
				}
			}
		}
	}
	
	public boolean isCollidingWithPlayer() {
		Rectangle currentEnemy = new Rectangle(this.getX() + maskx, this.getY() + masky, maskw, maskh);
		Rectangle player = new Rectangle(Game.player.getX(), Game.player.getY(), 16, 16);
		
		return currentEnemy.intersects(player);
	}
	
	public boolean isColliding(int xnext, int ynext) {
		Rectangle currentEnemy = new Rectangle(xnext + maskx, ynext + masky, maskw, maskh);
		
		for(int i = 0; i < Game.enemies.size(); i++) {
			Enemy e = Game.enemies.get(i);
			
			if(e == this)
				continue;
			
			Rectangle targetEnemy = new Rectangle(e.getX() + maskx, e.getY() + masky, maskw, maskh);
			
			if(currentEnemy.intersects(targetEnemy)) {
				return true;
			}
		}
		return false;
	}
}