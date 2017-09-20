package com.example.mr_zyl.project.pro.newpost.view.selfview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

import com.example.mr_zyl.project.R;
import com.example.mr_zyl.project.pro.newpost.bean.Tree;
import com.example.mr_zyl.project.utils.DensityUtil;
import com.example.mr_zyl.project.utils.DisplayUtil;
import com.example.mr_zyl.project.utils.GeometryUtil;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by TFHR02 on 2017/9/14.
 */
public class TreeGroup extends View {
    private static final String TAG = "TreeGroup";
    private List<Tree> treeDatas;
    private Context context;
    private float cellWidth, cellHeight;
    private float cellStrokeWidth;
    private Paint mPaint;
    private int width, height;
    private GestureDetectorCompat gesture;
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
    /**
     * 画布X轴的平移，用于实现曲线图的滚动效果
     */
    private float translateX = 0;
    /**
     * 画布Y轴的平移，用于实现曲线图的滚动效果
     */
    private float translateY = 0;

    /**
     * 是不是第一次绘制
     */
    boolean is_firstdraw = true;
    private int minX, maxX;

    private int minY, maxY;
    /**
     * 滚动计算器
     */
    private Scroller scroller;

    /**
     * 上一个被选中的项的下标
     */
    private int last_selected_index;

    public TreeGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        scroller = new Scroller(context);
        gesture = new GestureDetectorCompat(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                getParent().requestDisallowInterceptTouchEvent(true);
                Log.i(TAG, "onScroll: " + distanceX);
                float transx = translateX - 1.5f * distanceX;
                float transy = translateY - 1.5f * distanceY;
                if (transx < minX || transx > maxX) {
                    return false;
                }
                if (transy < minY || transy > maxY) {
                    return false;
                }
                moveTo((int) transx, (int) transy);
                return true;
            }

            @Override
            public boolean onDown(MotionEvent e) {
                //强制结束本次滑屏操作
                scroller.forceFinished(true);
                ViewCompat.postInvalidateOnAnimation(TreeGroup.this);
                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                scroller.fling((int) getTranslateX(), (int) getTranslateY(), (int) velocityX, (int) velocityY, minX, maxX, minY, maxY);
                ViewCompat.postInvalidateOnAnimation(TreeGroup.this);
                return false;
            }
        });
        cellWidth = cellHeight = DensityUtil.getpxByDimensize(context, R.dimen.x180);
        cellStrokeWidth = DensityUtil.getpxByDimensize(context, R.dimen.x6);
        cellVerticalDistance = cellHorizontalDistance = DensityUtil.getpxByDimensize(context, R.dimen.y450);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(getResources().getColor(R.color.green));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setTextSize(45);
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            moveTo(scroller.getCurrX(), scroller.getCurrY());
        }
    }

    /**
     * 平移画布
     *
     * @param x
     */
    public void moveTo(int x, int y) {
        translateX = x;
        translateY = y;
        ViewCompat.postInvalidateOnAnimation(this);
    }

    public float getTranslateX() {
        return translateX;
    }

    public float getTranslateY() {
        return translateY;
    }

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

    public void setData(LinkedList<Tree> Datas) {
        this.treeDatas = Datas;
        this.is_firstdraw = true;
        cellDATAS = new ArrayList<>();

        //获取水平方向最多有多少圆
        int level_maxcount = 0;
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
                            + cellHeight / 2, tree.getText(), false);
            cellDATAS.add(point);
        }
        requestLayout();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (is_firstdraw) {
            minX = -(width - DisplayUtil.Width(context)) / 2;
            maxX = (width - DisplayUtil.Width(context)) / 2;

            minY = -(height - DisplayUtil.Width(context)) / 2;
            maxY = (height - DisplayUtil.Width(context)) / 2;

            this.translateX = maxX;
            this.translateY = maxY;
            is_firstdraw = false;
        }
        canvas.translate(getTranslateX(), getTranslateY());
        for (int i = 0; i < cellDATAS.size(); i++) {
            cellPoint point = cellDATAS.get(i);
            //画圆
            mPaint.setStrokeWidth(cellStrokeWidth);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setColor(getResources().getColor(R.color.green));
            canvas.drawCircle(point.x, point.y, cellWidth / 2, mPaint);
            //画图标
            String url = treeDatas.get(i).getUrl();
            if (url != null) {
            }
            //画文本
            mPaint.setStrokeWidth(cellStrokeWidth);
            mPaint.setStyle(Paint.Style.FILL);
            String text = point.text;
            Rect textrect = new Rect();
            mPaint.getTextBounds(text, 0, text.length(), textrect);//测量文本
            mPaint.setColor(point.is_selected ? Color.RED : getResources().getColor(R.color.green));//选中的字体为红色
            canvas.drawText(text, point.x - textrect.width() / 2, point.y + textrect.height() / 2, mPaint);
            //画连线(当前圆 和 子结点圆相连线)
            List<cellPoint> childpoints = getChildTreesById(i);  //获取所有的子节点圆
            for (int c = 0; c < childpoints.size(); c++) {
                cellPoint child_point = childpoints.get(c);
                mPaint.setColor(getResources().getColor(R.color.green));
                draw_nocross_ciclecenter_lines(canvas, point, child_point, mPaint);
            }

            //画选中的分支线
            if (point.is_selected) {
                Tree tree = treeDatas.get(point.pos);
                if (tree == null) {
                    return;
                }
                for (int k = 0; k < getMaxLevel(); k++) {

                    if (tree.getParent() == null) {
                        break;
                    }
                    int parent_id = tree.getParent().getId();
                    int the_id = tree.getId();
                    mPaint.setColor(Color.RED);
                    draw_nocross_ciclecenter_lines(canvas, cellDATAS.get(parent_id), cellDATAS.get(the_id), mPaint);
                    tree = tree.getParent();
                }

            }
        }


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                float x = event.getX();
                float y = event.getY();

                //把之前的选中的设置不选中
                cellDATAS.get(last_selected_index).is_selected = false;
                //在设置选中
                for (int i = 0; i < cellDATAS.size(); i++) {
                    cellPoint cellPoint = cellDATAS.get(i);
                    float distance = GeometryUtil.getDistanceBetween2Points(new PointF(cellPoint.x, cellPoint.y), new PointF(x - translateX, y - translateY));
                    if (distance < cellWidth / 2) {
                        cellPoint.is_selected = true;
                        last_selected_index = i;
                        break;
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
        }

        return gesture.onTouchEvent(event);
    }


    /**
     * 是否有子节点被选中
     *
     * @param point
     * @return
     */
    private boolean is_has_childSelected(cellPoint point) {
        boolean is_has = false;
        //根据选中的点，往上获取parent，直到顶部。将当前圆和parent圆，之间的线变色。

        return is_has;
    }


    /**
     * 绘制，不过圆心的两点连线
     *
     * @param canvas 画布
     * @param p1     第一个点
     * @param p2     第二个点
     */
    private void draw_nocross_ciclecenter_lines(Canvas canvas, cellPoint p1, cellPoint p2, Paint mpaint) {
        boolean is_direction_bottom=p2.y>p1.y?true:false;
        //当前圆圆心
        PointF start_point, end_point;

        //直线的斜率
        double tan = (p1.y - p2.y) / (p1.x - p2.x);

        //下个圆圆心与当前圆圆心连线直线 与 当前圆的交点
        PointF[] rectF1 = GeometryUtil.getIntersectionPoints(new PointF(p1.x, p1.y), cellWidth / 2, 1 / tan,is_direction_bottom);
        if (tan > 0) {
            start_point = rectF1[1];
        } else {
            start_point = rectF1[0];
        }
        //下个圆圆心与当前圆圆心连线直线 与 下个圆的交点
        PointF[] rectF2 = GeometryUtil.getIntersectionPoints(new PointF(p2.x, p2.y), cellWidth / 2, 1 / tan,is_direction_bottom);
        if (tan > 0) {
            end_point = rectF2[0];
        } else {
            end_point = rectF2[1];
        }
        canvas.drawLine(start_point.x, start_point.y, end_point.x, end_point.y, mpaint);
    }

    /**
     * 根据层级，获取当前层 圆的数量
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

    /**
     * 根据id，获取所有的子节点圆
     *
     * @param id
     * @return
     */
    private List<cellPoint> getChildTreesById(int id) {
        List<cellPoint> childPoints = new ArrayList<>();
        for (int i = 0; i < treeDatas.size(); i++) {
            Tree tree = treeDatas.get(i);
            if (tree.getPid() == id) {
                childPoints.add(cellDATAS.get(i));
            }
        }
        return childPoints;
    }

    class cellPoint {
        public int pos;
        public float x;
        public float y;
        public String text;
        public boolean is_selected = false;

        public cellPoint(int pos, float x, float y, String text, boolean is_selected) {
            this.pos = pos;
            this.x = x;
            this.y = y;
            this.text = text;
            this.is_selected = is_selected;
        }
    }
}
