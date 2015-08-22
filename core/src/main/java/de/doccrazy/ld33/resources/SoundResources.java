package de.doccrazy.ld33.resources;

import com.badlogic.gdx.audio.Sound;

import de.doccrazy.shared.core.ResourcesBase;

public class SoundResources extends ResourcesBase {
	public Sound fire = sound("fire.wav");
	public Sound[] hit = new Sound[] { sound("hit1.wav"), sound("hit2.wav"), sound("hit3.wav") };
	public Sound block = sound("block.wav");
	public Sound jump = sound("jump.wav");
	public Sound shotHit = sound("shot-hit.wav");
	public Sound shotHitWall = sound("shot-hitwall.wav");
    public Sound shotCharge = sound("shot-charge.wav");
}
