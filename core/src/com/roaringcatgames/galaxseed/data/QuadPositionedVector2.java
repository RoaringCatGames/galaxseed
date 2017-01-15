package com.roaringcatgames.galaxseed.data;

import com.badlogic.gdx.math.Vector2;

/**
 * Composed Position Class so we can use the positionOffset
 *  from the defined Quadrant origin.
 */
public class QuadPositionedVector2 extends Vector2{
    private Vector2 quadAdjust = new Vector2();
    public OffsetQuadrant offsetQuadrant = OffsetQuadrant.QUAD_3;

    public QuadPositionedVector2(){
        super();
    }

    public QuadPositionedVector2(float x, float y){
        super(x, y);
    }

    public QuadPositionedVector2(float x, float y, OffsetQuadrant offsetQuad){
        super(x, y);
        this.offsetQuadrant = offsetQuad;
    }


    public Vector2 quadAdjusted(float w, float h){
        switch(offsetQuadrant){
            case CENTER:
                quadAdjust.set(this.x + (w/2f), this.y + (h/2f));
                break;
            case CENTER_X:
                quadAdjust.set(this.x + (w/2f), this.y);
                break;
            case CENTER_Y:
                quadAdjust.set(this.x, this.y + (h/2f));
                break;
            case QUAD_1:
                quadAdjust.set(w + this.x, h + this.y);
                break;
            case QUAD_2:
                quadAdjust.set(this.x, h + this.y);
                break;
            case QUAD_3:
                quadAdjust.set(this.x, this.y);
                break;
            case QUAD_4:
                quadAdjust.set(w + this.x, this.y);
                break;
            default:
                quadAdjust.set(this.x, this.y);
                break;
        }

        return quadAdjust;
    }
}
