package de.lfisch.guardofgreece.handlers;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

public class MyInputProcessor extends InputAdapter {

    public boolean mouseMoved(int x, int y) {
        MyInput.x = x;
        MyInput.y = y;
        return true;
    }

    public boolean touchDragged(int x, int y, int pointer) {
        MyInput.x = x;
        MyInput.y = y;
        MyInput.down = true;
        return true;
    }

    public boolean touchDown(int x, int y, int pointer, int button) {
        MyInput.x = x;
        MyInput.y = y;
        MyInput.down = true;
        return true;
    }

    public boolean touchUp(int x, int y, int pointer, int button) {
        MyInput.x = x;
        MyInput.y = y;
        MyInput.down = false;
        return true;
    }

    public boolean keyDown(int k) {
        if(k == Input.Keys.SPACE) MyInput.setKey(MyInput.BUTTON1, true);
        if(k == Input.Keys.D) MyInput.setKey(MyInput.BUTTON2, true);
        if(k == Input.Keys.A) MyInput.setKey(MyInput.BUTTON3, true);
        if(k == Input.Keys.SHIFT_LEFT) MyInput.setKey(MyInput.BUTTON4, true);
        if(k == Input.Keys.LEFT) MyInput.setKey(MyInput.BUTTON5, true);
        if(k == Input.Keys.RIGHT) MyInput.setKey(MyInput.BUTTON6, true);
        if(k == Input.Keys.NUMPAD_0) MyInput.setKey(MyInput.BUTTON7, true);
        if(k == Input.Keys.UP) MyInput.setKey(MyInput.BUTTON8, true);
        if(k == Input.Keys.W) MyInput.setKey(MyInput.BUTTON9, true);
        if(k == Input.Keys.S) MyInput.setKey(MyInput.BUTTON10, true);
        return true;
    }

    public boolean keyUp(int k) {
        if(k == Input.Keys.SPACE) MyInput.setKey(MyInput.BUTTON1, false);
        if(k == Input.Keys.D) MyInput.setKey(MyInput.BUTTON2, false);
        if(k == Input.Keys.A) MyInput.setKey(MyInput.BUTTON3, false);
        if(k == Input.Keys.SHIFT_LEFT) MyInput.setKey(MyInput.BUTTON4, false);
        if(k == Input.Keys.LEFT) MyInput.setKey(MyInput.BUTTON5, false);
        if(k == Input.Keys.RIGHT) MyInput.setKey(MyInput.BUTTON6, false);
        if(k == Input.Keys.NUMPAD_0) MyInput.setKey(MyInput.BUTTON7, false);
        if(k == Input.Keys.NUMPAD_COMMA) MyInput.setKey(MyInput.BUTTON8, false);
        if(k == Input.Keys.W) MyInput.setKey(MyInput.BUTTON9, false);
        if(k == Input.Keys.S) MyInput.setKey(MyInput.BUTTON10, false);
        return true;
    }


}


