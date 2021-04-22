package otp.group6.audioeditor;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import be.tarsos.dsp.resample.RateTransposer;
import otp.group6.controller.Controller;
import otp.group6.view.MainController;

class AudioManipulatorTest {
	

	private AudioManipulator am = new AudioManipulator(null);
	
	

	@Test
	void testAudioManipulator() {
		fail("Not yet implemented");
	}

	@Test
	void testSetAudioSourceFile() {
		fail("Not yet implemented");
	}

	@Test
	@DisplayName("Set pitch factor")
	void testSetPitchFactor() {
	 RateTransposer rateTransposer = new RateTransposer(0);
	 
		am.setPitchFactor(2.4);
		assertEquals(2.4, am.getEffectValue("pitch"));
	}

	@Test
	void testSetGain() {
		fail("Not yet implemented");
	}

	@Test
	void testSetEchoLength() {
		fail("Not yet implemented");
	}

	@Test
	void testSetDecay() {
		fail("Not yet implemented");
	}

	@Test
	void testSetFlangerLength() {
		fail("Not yet implemented");
	}

	@Test
	void testSetWetness() {
		fail("Not yet implemented");
	}

	@Test
	void testSetLFO() {
		fail("Not yet implemented");
	}

	@Test
	void testSetLowPass() {
		fail("Not yet implemented");
	}

	@Test
	void testDisablePitchEffect() {
		fail("Not yet implemented");
	}

	@Test
	void testDisableGainEffect() {
		fail("Not yet implemented");
	}

	@Test
	void testDisableDelayEffect() {
		fail("Not yet implemented");
	}

	@Test
	void testDisableFlangerEffect() {
		fail("Not yet implemented");
	}

	@Test
	void testDisableLowPassEffect() {
		fail("Not yet implemented");
	}

	@Test
	void testTestFilter() {
		fail("Not yet implemented");
	}

	@Test
	void testPlayAudio() {
		fail("Not yet implemented");
	}

	@Test
	void testPlayFromDesiredSec() {
		fail("Not yet implemented");
	}

	@Test
	void testStopAudio() {
		fail("Not yet implemented");
	}

	@Test
	void testPauseAudio() {
		fail("Not yet implemented");
	}

	@Test
	void testAudioFileReachedEnd() {
		fail("Not yet implemented");
	}

	@Test
	void testResetMediaPlayer() {
		fail("Not yet implemented");
	}

	@Test
	void testTimerCancel() {
		fail("Not yet implemented");
	}

	@Test
	void testSetAudioFileLengthInSec() {
		fail("Not yet implemented");
	}

	@Test
	void testUsePitchProcessor() {
		fail("Not yet implemented");
	}

	@Test
	void testUseGainProcessor() {
		fail("Not yet implemented");
	}

	@Test
	void testUseDelayProcessor() {
		fail("Not yet implemented");
	}

	@Test
	void testUseFlangerProcessor() {
		fail("Not yet implemented");
	}

	@Test
	void testUseLowPassProcessor() {
		fail("Not yet implemented");
	}

	@Test
	void testSaveFile() {
		fail("Not yet implemented");
	}

	@Test
	void testRecordAudio() {
		fail("Not yet implemented");
	}

	@Test
	void testStopRecord() {
		fail("Not yet implemented");
	}

}
