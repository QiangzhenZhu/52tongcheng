package cn.xcom.banjing.chat;

/**
 * Created by mac on 2017/10/3.
 */


public class ChatItemBean {

    /**
     * id : 11
     * send_uid : 2
     * receive_uid : 1
     * send_type : 1
     * content_type : 0
     * content : No
     * extra_info_id : 0
     * read_status : 1
     * status : 0
     * create_time : 1499172170
     * recive_type : 1
     * send_face : avatar14991690412.png
     * send_nickname : 高先生
     * receive_face : yyy1497487820172.jpg
     * receive_nickname : 糖友1
     * audioInfo : {"audio_id":"16","audio_url":"uploads/audio/chat14993101332.mp3","duration":"1","key_hash":"8de7ed77ac406c986aa60b9d44ba2f3b","ctime":"1499310133"}
     * picInfo : {"image_id":"6","original_url":"uploads/images/chat14993104552.png","original_height":"3000","original_width":"2250","thumb_url":"uploads/images_thumb/chat14993104552.png","thumb_height":"720","thumb_width":"540","type":"0","time":"0"}
     */

    private String id;
    private String send_uid;
    private String receive_uid;
    private String send_type;
    private String content_type;
    private String content;
    private String extra_info_id;
    private String read_status;
    private String status;
    private String create_time;
    private String recive_type;
    private String send_face;
    private String send_nickname;
    private String receive_face;
    private String receive_nickname;
    private AudioInfoBean audioInfo;
    private PicInfoBean picInfo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSend_uid() {
        return send_uid;
    }

    public void setSend_uid(String send_uid) {
        this.send_uid = send_uid;
    }

    public String getReceive_uid() {
        return receive_uid;
    }

    public void setReceive_uid(String receive_uid) {
        this.receive_uid = receive_uid;
    }

    public String getSend_type() {
        return send_type;
    }

    public void setSend_type(String send_type) {
        this.send_type = send_type;
    }

    public String getContent_type() {
        return content_type;
    }

    public void setContent_type(String content_type) {
        this.content_type = content_type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getExtra_info_id() {
        return extra_info_id;
    }

    public void setExtra_info_id(String extra_info_id) {
        this.extra_info_id = extra_info_id;
    }

    public String getRead_status() {
        return read_status;
    }

    public void setRead_status(String read_status) {
        this.read_status = read_status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getRecive_type() {
        return recive_type;
    }

    public void setRecive_type(String recive_type) {
        this.recive_type = recive_type;
    }

    public String getSend_face() {
        return send_face;
    }

    public void setSend_face(String send_face) {
        this.send_face = send_face;
    }

    public String getSend_nickname() {
        return send_nickname;
    }

    public void setSend_nickname(String send_nickname) {
        this.send_nickname = send_nickname;
    }

    public String getReceive_face() {
        return receive_face;
    }

    public void setReceive_face(String receive_face) {
        this.receive_face = receive_face;
    }

    public String getReceive_nickname() {
        return receive_nickname;
    }

    public void setReceive_nickname(String receive_nickname) {
        this.receive_nickname = receive_nickname;
    }

    public AudioInfoBean getAudioInfo() {
        return audioInfo;
    }

    public void setAudioInfo(AudioInfoBean audioInfo) {
        this.audioInfo = audioInfo;
    }

    public PicInfoBean getPicInfo() {
        return picInfo;
    }

    public void setPicInfo(PicInfoBean picInfo) {
        this.picInfo = picInfo;
    }

    public static class AudioInfoBean {
        /**
         * audio_id : 16
         * audio_url : uploads/audio/chat14993101332.mp3
         * duration : 1
         * key_hash : 8de7ed77ac406c986aa60b9d44ba2f3b
         * ctime : 1499310133
         */

        private String audio_id;
        private String audio_url;
        private String duration;
        private String key_hash;
        private String ctime;

        public String getAudio_id() {
            return audio_id;
        }

        public void setAudio_id(String audio_id) {
            this.audio_id = audio_id;
        }

        public String getAudio_url() {
            return audio_url;
        }

        public void setAudio_url(String audio_url) {
            this.audio_url = audio_url;
        }

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public String getKey_hash() {
            return key_hash;
        }

        public void setKey_hash(String key_hash) {
            this.key_hash = key_hash;
        }

        public String getCtime() {
            return ctime;
        }

        public void setCtime(String ctime) {
            this.ctime = ctime;
        }
    }

    public static class PicInfoBean {
        /**
         * image_id : 6
         * original_url : uploads/images/chat14993104552.png
         * original_height : 3000
         * original_width : 2250
         * thumb_url : uploads/images_thumb/chat14993104552.png
         * thumb_height : 720
         * thumb_width : 540
         * type : 0
         * time : 0
         */

        private String image_id;
        private String original_url;
        private String original_height;
        private String original_width;
        private String thumb_url;
        private String thumb_height;
        private String thumb_width;
        private String type;
        private String time;

        public String getImage_id() {
            return image_id;
        }

        public void setImage_id(String image_id) {
            this.image_id = image_id;
        }

        public String getOriginal_url() {
            return original_url;
        }

        public void setOriginal_url(String original_url) {
            this.original_url = original_url;
        }

        public String getOriginal_height() {
            return original_height;
        }

        public void setOriginal_height(String original_height) {
            this.original_height = original_height;
        }

        public String getOriginal_width() {
            return original_width;
        }

        public void setOriginal_width(String original_width) {
            this.original_width = original_width;
        }

        public String getThumb_url() {
            return thumb_url;
        }

        public void setThumb_url(String thumb_url) {
            this.thumb_url = thumb_url;
        }

        public String getThumb_height() {
            return thumb_height;
        }

        public void setThumb_height(String thumb_height) {
            this.thumb_height = thumb_height;
        }

        public String getThumb_width() {
            return thumb_width;
        }

        public void setThumb_width(String thumb_width) {
            this.thumb_width = thumb_width;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }
}

