package de.doccrazy.ld33.resources;

import com.badlogic.gdx.audio.Sound;

import de.doccrazy.shared.core.ResourcesBase;

public class SoundResources extends ResourcesBase {
	public Sound select = sound("select.wav");
	public Sound[] twang = new Sound[] { sound("twang.wav"), sound("twang2.wav"), sound("twang3.wav") };
	public Sound[] breakThread = new Sound[] { sound("break1.wav"), sound("break2.wav"), sound("break3.wav") };
	public Sound catchFly = sound("catch.wav");
	public Sound jump = sound("jump.wav");
	public Sound grab = sound("grab.wav");
}
