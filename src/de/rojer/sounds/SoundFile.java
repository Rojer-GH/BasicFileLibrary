/**
 * 
 */
package de.rojer.sounds;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Use this class to load in an audio file (.wav format!)
 * @author Rojer
 * @version 18.03.2019
 */
public class SoundFile{

	/**
	 * The Sound
	 */
	protected Clip sound;

	/**
	 * All controls supported
	 */
	protected FloatControl gain, volume, pan, balance, auxSend, auxReturn, reverbSend, reverbReturn;

	//Constructors
	
	/**
	 * Load in an audio file
	 * @param path the path to the file
	 * @param fileName the name of the file (with extension!)
	 */
	public SoundFile(String path, String fileName){ this(path + "/" + fileName); }

	/**
	 * Load in an audio file
	 * @param fullPath the path to the file with the name of the file already
	 *        included (Basically: path + "/" + fileName)
	 */
	public SoundFile(String fullPath){
		try{
			InputStream audioSource = this.getClass().getResourceAsStream(fullPath);
			InputStream bufferedInput = new BufferedInputStream(audioSource);
			AudioInputStream ais = AudioSystem.getAudioInputStream(bufferedInput);
			AudioFormat baseFormat = ais.getFormat();
			AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(), baseFormat.getChannels() * 2,
					baseFormat.getSampleRate(), false);
			AudioInputStream dais = AudioSystem.getAudioInputStream(decodedFormat, ais);
			this.sound = AudioSystem.getClip();
			this.sound.open(dais);
			this.gain			= (FloatControl)this.sound.getControl(FloatControl.Type.MASTER_GAIN);
			this.volume			= (FloatControl)this.sound.getControl(FloatControl.Type.VOLUME);
			this.pan			= (FloatControl)this.sound.getControl(FloatControl.Type.PAN);
			this.balance		= (FloatControl)this.sound.getControl(FloatControl.Type.BALANCE);
			this.auxSend		= (FloatControl)this.sound.getControl(FloatControl.Type.AUX_SEND);
			this.auxReturn		= (FloatControl)this.sound.getControl(FloatControl.Type.AUX_RETURN);
			this.reverbSend		= (FloatControl)this.sound.getControl(FloatControl.Type.REVERB_SEND);
			this.reverbReturn	= (FloatControl)this.sound.getControl(FloatControl.Type.REVERB_RETURN);
		}catch(UnsupportedAudioFileException | IOException | LineUnavailableException e){
			e.printStackTrace();
		}
	}
	
	//"Destrucotrs"
	
	/**
	 * Destroys this object
	 * @param file this sound file
	 */
	public void destroy(SoundFile file) {
		this.close();
		this.sound = null;
		this.auxReturn = null;
		this.auxSend = null;
		this.balance = null;
		this.gain = null;
		this.pan = null;
		this.reverbReturn = null;
		this.reverbSend = null;
		this.volume = null;
		file = null;
	}

	/**
	 * Starts playing the sound
	 */
	public void play(){
		if (this.sound == null){ return; }
		this.stop();
		this.sound.setFramePosition(0);
		while (!this.sound.isRunning()){
			this.sound.start();
		}
	}

	/**
	 * Stops the sound
	 */
	public void stop(){ if (this.sound.isRunning()){ this.sound.stop(); } }

	/**
	 * Use this method to delete the sound from the RAM
	 */
	public void close(){
		this.stop();
		this.sound.drain();
		this.sound.close();
	}

	/**
	 * Loops the sound infinite times
	 */
	public void loop(){
		this.sound.loop(Clip.LOOP_CONTINUOUSLY);
		this.play();
	}

	/**
	 * Sets the gain value of the sound
	 * @param gain the gain value
	 */
	public void setGain(float gain){ this.gain.setValue(gain); }

	/**
	 * Sets the volume to play at
	 * @param volume the volume
	 */
	public void setVolume(float volume){ this.volume.setValue(volume); }

	/**
	 * Sets the "position" of the sound, also works for mono
	 * @param pan the "position"
	 */
	public void setPan(float pan){ this.pan.setValue(pan); }

	/**
	 * Sets the "position" of the sound
	 * @param balance the "position"
	 */
	public void setBalance(float balance){ this.balance.setValue(balance); }

	/**
	 * Sets the gain value of the auxiliary send line
	 * @param auxSend the gain value
	 */
	public void setAuxSend(float auxSend){ this.auxSend.setValue(auxSend); }

	/**
	 * Sets the gain value of the auxiliary return line
	 * @param auxReturn the gain value
	 */
	public void setAuxReturn(float auxReturn){ this.auxReturn.setValue(auxReturn); }

	/**
	 * Sets the pre-reverb gain value
	 * @param reverbSend the gain value
	 */
	public void setReverbSend(float reverbSend){ this.reverbSend.setValue(reverbSend); }

	/**
	 * Sets the post-reverb gain value
	 * @param reverbReturn the gain value
	 */
	public void setReverbReturn(float reverbReturn){ this.reverbReturn.setValue(reverbReturn); }

	/**
	 * @return true if the sound is currently being played
	 */
	public boolean isPlaying(){ return sound.isRunning(); }

}
