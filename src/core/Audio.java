package core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.zkoss.util.media.Media;
import org.zkoss.zk.au.out.AuInvoke;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.ext.render.DynamicMedia;

import org.zkoss.zul.impl.XulElement;
import org.zkoss.zul.impl.Utils;

public class Audio extends XulElement {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected List<String> _src = new ArrayList<String>();
    private org.zkoss.sound.Audio _audio;
    private byte _audver;
    private boolean _autoplay, _controls, _loop, _muted;
    private String _preload;
    
    //public Audio(String src) {
    //    setSrc(src);
    //}

    public void play() {
        response("ctrl", new AuInvoke(this, "play"));
    }
    public void stop() {
        response("ctrl", new AuInvoke(this, "stop"));
    }
    public void pause() {
        response("ctrl", new AuInvoke(this, "pause"));
    }

    public List<String> getSrc() {
        return _src;
    }
    public void setSrc(String src) {
        List<String> list = new ArrayList<String>();  
        if (src.contains(",")) {
            list = new ArrayList<String>(Arrays.asList(src.split("\\s*,\\s*"))); 
        } else {
            list.add(src.trim());
        }
        if (_audio != null || !_src.equals(list)) {
            _audio = null;
            setSrc(list);
        }
        
    }
    public void setSrc(List<String> src) {
        if (!src.equals(_src)) {
            _src = src;
            smartUpdate("src", new EncodedSrc());
        }
    }

    public boolean isAutostart() {
        return isAutoplay();
    }
    public void setAutostart(boolean autostart) {
        setAutoplay(autostart);
    }
    public boolean isAutoplay() {
       return _autoplay;
    }
    public void setAutoplay(boolean autoplay) {
        if (_autoplay != autoplay) {
           _autoplay = autoplay;
           smartUpdate("autoplay", _autoplay);
        }
    }
    public String getPreload() {
        return _preload;
    }
    public void setPreload(String preload) {
        if ("none".equalsIgnoreCase(preload)) {
            preload = "none";
        } else if ("metadata".equalsIgnoreCase(preload)){
            preload = "metadata";
        } else {
            preload = "auto";
        }
        if (!preload.equals(_preload)) {
            _preload = preload;
            smartUpdate("preload", _preload);
        }
    }
    public boolean isControls() {
        return _controls;
    }
    public void setControls(boolean controls) {
        if (_controls != controls) {
            _controls = controls;
            smartUpdate("controls", _controls);
        }
    }
    public boolean isLoop() {
        return _loop;
    }
    public void setLoop(boolean loop) {
        if (_loop != loop) {
            _loop = loop;
            smartUpdate("loop", _loop);
        }
    }
    public boolean isMuted() {
        return _muted;
    }
    public void setMuted(boolean muted) {
        if (_muted != muted) {
            _muted = muted;
            smartUpdate("muted", _muted);
        }
    }
    public void setContent(org.zkoss.sound.Audio audio) {
        if (_src != null || audio != _audio) {
            _audio = audio;
            _src = null;
            if (_audio != null) ++_audver; //enforce browser to reload
             smartUpdate("src", new EncodedSrc());
        }
    }
    public org.zkoss.sound.Audio getContent() {
        return _audio;
    }
    
    private List<String> getEncodedSrc() {
        final Desktop dt = getDesktop();
        List<String> list = new ArrayList<String>();
        if(_audio != null) {
            list.add(getAudioSrc());
        } else if (dt != null) {
            for(String src: _src) {
                list.add(dt.getExecution().encodeURL(src));
            }
        }
        return list;
    }

    private String getAudioSrc() {
        return Utils.getDynamicMediaURI(
            this, _audver, _audio.getName(), _audio.getFormat());
    }

    // super
    protected void renderProperties(org.zkoss.zk.ui.sys.ContentRenderer renderer)
    throws java.io.IOException {
        super.renderProperties(renderer);

        render(renderer, "src", getEncodedSrc());
        render(renderer, "autoplay", _autoplay);
        render(renderer, "preload", _preload);
        render(renderer, "controls", _controls);
        render(renderer, "loop", _loop);
        render(renderer, "muted", _muted);
    }

    protected boolean isChildable() {
        return false;
    }
    
    // ComponentCtrl
    public Object getExtraCtrl() {
        return new ExtraCtrl();
    }
    protected class ExtraCtrl extends XulElement.ExtraCtrl
    implements DynamicMedia {
        //-- DynamicMedia --//
        public Media getMedia(String pathInfo) {
            return _audio;
        }
    }

    private class EncodedSrc implements org.zkoss.zk.au.DeferredValue {
        public Object getValue() {
            return getEncodedSrc();
        }
    }
}