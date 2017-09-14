package com.example.mr_zyl.project.pro.newpost.view.selfview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.example.mr_zyl.project.R;
import com.example.mr_zyl.project.pro.newpost.bean.Tree;
import com.example.mr_zyl.project.utils.DensityUtil;
import com.example.mr_zyl.project.utils.DisplayUtil;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by TFHR02 on 2017/9/14.
 */
public class TreeGroup extends View {
    private List<Tree> treeDatas;
    private Context context;
    private float cellWidth, cellHeight;
    private float cellStrokeWidth;
    private Paint mPaint;
    private int width, height;
    /**
     * 所有的圆
     */
    private List<cellPoint> cellDATAS = null;
    /**
     * 圆之间水平方向上的间距
     */
    private int cellHorizontalDistance;
    /**
     * 圆之间竖直方向上的间距
     */
    private int cellVerticalDistance;

    public TreeGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        cellWidth = cellHeight = DensityUtil.getpxByDimensize(context, R.dimen.x90);
        cellStrokeWidth = DensityUtil.getpxByDimensize(context, R.dimen.x6);
        cellVerticalDistance = cellHorizontalDistance = DensityUtil.getpxByDimensize(context, R.dimen.y120);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(getResources().getColor(R.color.green));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setTextSize(45);
    }

    int level_maxcount = 0;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (treeDatas == null) {
            width = getDefaultSize(DisplayUtil.Width(context), widthMeasureSpec);
            height = getDefaultSize(DisplayUtil.Height(context), heightMeasureSpec);
        } else {
            int maxlevel = getMaxLevel();
            height = (int) (cellHeight * (maxlevel + 1) + cellVerticalDistance * maxlevel + cellStrokeWidth * 2);
        }
        setMeasuredDimension(width, height);
    }

    /**
     * 根据层级，获取当前层数量
     *
     * @param level
     * @return
     */
    private int getcountByLevel(int level) {
        int count = 0;
        for (int i = 0; i < treeDatas.size(); i++) {
            if (treeDatas.get(i).getLevel() == level) {
                count = count + 1;
            }
        }
        return count;
    }

    /**
     * 获取最大的层级
     *
     * @return
     */
    private int getMaxLevel() {
        int maxlevel = 0;
        for (int i = 0; i < treeDatas.size(); i++) {
            int level = treeDatas.get(i).getLevel();
            maxlevel = level > maxlevel ? level : maxlevel;
        }
        return maxlevel;
    }

    public void setData(LinkedList<Tree> Datas) {
        this.treeDatas = Datas;
        cellDATAS = new ArrayList<>();

        //获取水平方向最多有多少圆
        for (int i = 0; i < treeDatas.size(); i++) {
            Tree tree = treeDatas.get(i);
            int level = tree.getLevel();
            int level_count = getcountByLevel(level);
            level_maxcount = level_count > level_maxcount ? level_count : level_maxcount;
        }

        width = (int) (cellWidth * level_maxcount + cellHorizontalDistance * (level_maxcount - 1) + cellStrokeWidth * 2);

        for (int i = 0; i < treeDatas.size(); i++) {
            Tree tree = treeDatas.get(i);
            //计算当前圆左边有多少个圆，left_count
            int left_count = getleft_cells(tree.getId(), tree.getLevel());
            //计算当前圆上边有多少个圆，top_count
            int top_count = tree.getLevel();

            int level_count = getcountByLevel(tree.getLevel());
            //x:左边内边距+当前圆距离左边界距离+圆一半
            cellPoint point = new cellPoint(i,
                             (width - level_count * cellWidth - (level_count - 1) * cellHorizontalDistance) / 2
                            + (cellWidth + cellHorizontalDistance) * left_count
                            + cellWidth / 2
                    ,
                    cellStrokeWidth
                            + (cellWidth + cellVerticalDistance) * top_count
                            + cellHeight / 2);
            cellDATAS.add(point);
        }
        requestLayout();
    }

    /**
     * 获取当前圆左边有多少圆
     *
     * @param id
     * @param level
     * @return
     */
    private int getleft_cells(int id, int level) {
        int count = 0;
        for (int i = 0; i < treeDatas.size(); i++) {
            Tree tree = treeDatas.get(i);
            if (tree.getLevel() == level) {
                if (tree.getId() == id) {
                    break;
                }
                count = count + 1;
            }
        }
        return count;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0; i < cellDATAS.size(); i++) {
            cellPoint cellPoint = cellDATAS.get(i);
            mPaint.setStrokeWidth(cellStrokeWidth);
            mPaint.setStyle(Paint.Style.STROKE);
            canvas.drawCircle(cellPoint.x, cellPoint.y, cellWidth / 2, mPaint);

            mPaint.setStrokeWidth(2);
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawText(cellPoint.pos + "", cellPoint.x - 12, cellPoint.y + 12, mPaint);
        }
    }

    class cellPoint {
        public int pos;
        public float x;
        public float y;

        public cellPoint(int pos, float x, float y) {
            this.pos = pos;
            this.x = x;
            this.y = y;
        }
    }
}
