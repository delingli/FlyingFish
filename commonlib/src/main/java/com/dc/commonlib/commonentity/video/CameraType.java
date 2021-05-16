package com.dc.commonlib.commonentity.video;

public enum CameraType {
    /**
     * 枪机
     */
    GUN(0),
    /**
     * 半球
     */
    GLOBE_HALF(1),
    /**
     * 快球
     */
    GLOBE(2),
    /**
     * 带云镜枪机
     */
    GUN_MIRROR(3);

    private final int type;

    CameraType(int type) {
        this.type = type;
    }
}
