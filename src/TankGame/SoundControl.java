package TankGame;
import java.io.File;


import javax.sound.sampled.*;

class SoundControl {
    private static Clip music;
    private Clip fx;

    void MusicStart(String song) {
        try {
            AudioInputStream AIS;
            AIS = AudioSystem.getAudioInputStream(new File("Sources/" + song).getAbsoluteFile());
            music = AudioSystem.getClip();
            music.open(AIS);
            music.loop((Clip.LOOP_CONTINUOUSLY));
        }
        catch(Exception e) {
            System.err.println("****Unsupported Audio File****");
        }

        music.start();
    }

    void MusicStop() {
        music.start();
    }

    void outPutAudio(String sfx, float freq) {
        AudioInputStream AIS2;
        try {
            AIS2 = AudioSystem.getAudioInputStream(new File("Sources/" + sfx).getAbsoluteFile());
            fx = AudioSystem.getClip();
            fx.open(AIS2);
        }
        catch(Exception e) {
            System.err.println("****Unsupported Audio File****");
        }
        FloatControl gainControl = (FloatControl) fx.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(-freq);
        fx.start();
    }

}