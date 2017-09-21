package com.example.mr_zyl.project.pro.newpost.view.selfview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mr_zyl.project.R;
import com.example.mr_zyl.project.pro.newpost.bean.Tree;
import com.example.mr_zyl.project.utils.DensityUtil;
import com.example.mr_zyl.project.utils.DisplayUtil;
import com.example.mr_zyl.project.utils.GeometryUtil;
import com.example.mr_zyl.project.utils.SystemAppUtils;
import com.example.mr_zyl.project.utils.ToastUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by TFHR02 on 2017/9/19.
 */
public class TreeGroup2 extends ViewGroup {

    private static final String TAG = "TreeGroup";
    private List<Tree> treeDatas;
    private Context context;
    private float itemWidth, itemHeight;
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
    private int minX = 0, maxX = 0;

    private int minY = 0, maxY = 0;
    /**
     * 滚动计算器
     */
    private Scroller scroller;
    //view宽高是否满屏幕宽高
    private boolean is_NoFullScreenHeight, is_NoFullScreenWidth;

    public TreeGroup2(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        //设置默认参
        itemWidth = DensityUtil.getpxByDimensize(context, R.dimen.x120);
        itemHeight = DensityUtil.getpxByDimensize(context, R.dimen.x120);
        cellStrokeWidth = DensityUtil.getpxByDimensize(context, R.dimen.x6);
        cellVerticalDistance = cellHorizontalDistance = DensityUtil.getpxByDimensize(context, R.dimen.y300);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(getResources().getColor(R.color.green));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setTextSize(45);
        scroller = new Scroller(context);

        gesture = new GestureDetectorCompat(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                Log.i(TAG, "onScroll: " + distanceX);

                float transx = translateX - 1.5f * distanceX;
                float transy = translateY - 1.5f * distanceY;

                if (is_NoFullScreenHeight) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                    if (transx < minX || transx > maxX) {
                        return false;
                    }
                    if (Math.abs(distanceX / distanceY) > 1) {
                        moveTo((int) transx, (int) getTranslateY());
                        return true;
                    }
                }
                if (is_NoFullScreenWidth) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                    if (transy < minY || transy > maxY) {
                        return false;
                    }
                    if (Math.abs(distanceY / distanceX) > 1) {
                        moveTo((int) getTranslateX(), (int) transy);
                        return true;
                    }
                }
                if (!is_NoFullScreenHeight && !is_NoFullScreenWidth) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                    if (transx < minX || transx > maxX) {
                        return false;
                    }
                    if (transy < minY || transy > maxY) {
                        return false;
                    }
                    moveTo((int) transx, (int) transy);
                    return true;
                }
                return false;
            }

            @Override
            public boolean onDown(MotionEvent e) {
                //强制结束本次滑屏操作
                scroller.forceFinished(true);
                ViewCompat.postInvalidateOnAnimation(TreeGroup2.this);
                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                scroller.fling((int) getTranslateX(), (int) getTranslateY(), (int) velocityX, (int) velocityY, minX, maxX, minY, maxY);
                ViewCompat.postInvalidateOnAnimation(TreeGroup2.this);
                return false;
            }
        });
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

    public void setData(LinkedList<Tree> Datas) {
        this.treeDatas = Datas;
        this.is_firstdraw = true;

        LayoutInflater layoutinflater = LayoutInflater.from(context);
        for (int i = 0; i < treeDatas.size(); i++) {
            Tree tree = treeDatas.get(i);

            //添加view
            View view = layoutinflater.inflate(R.layout.newpost_treegroup_item, null);

            CircleImageView civ_newpost_treegroup = (CircleImageView) view.findViewById(R.id.civ_newpost_treegroup);
            TextView tv_newpost_treegroup = (TextView) view.findViewById(R.id.tv_newpost_treegroup);
            if (tree.getUrl() != null) {
                Picasso.with(context).load(tree.getUrl()).into(civ_newpost_treegroup);
            } else {
                Picasso.with(context).load(R.mipmap.ic_launcher).into(civ_newpost_treegroup);
            }
            tv_newpost_treegroup.setText(tree.getText());
            addView(view);
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        is_NoFullScreenWidth = false;
        is_NoFullScreenHeight = false;
        if (treeDatas == null || treeDatas.size() == 0) {
            width = getDefaultSize(DisplayUtil.Width(context), widthMeasureSpec);
            height = getDefaultSize(DisplayUtil.Height(context), heightMeasureSpec);
            setMeasuredDimension(width, height);
            return;
        }
        //测量子view
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        itemWidth = getChildAt(0).getMeasuredWidth();
        itemHeight = getChildAt(0).getMeasuredHeight();

        //获取水平方向最多有多少圆
        int level_maxcount = 0;
        for (int i = 0; i < treeDatas.size(); i++) {
            Tree tree = treeDatas.get(i);
            int level = tree.getLevel();
            int level_count = getcountByLevel(level);
            level_maxcount = level_count > level_maxcount ? level_count : level_maxcount;
        }
        //设置屏幕宽高
        width = (int) (itemWidth * level_maxcount + cellHorizontalDistance * (level_maxcount - 1) + cellStrokeWidth * 2);
        int maxlevel = getMaxLevel();
        height = (int) (itemHeight * (maxlevel + 1) + cellVerticalDistance * maxlevel + cellStrokeWidth * 2);
        if (width < DisplayUtil.Width(context)) {
            is_NoFullScreenWidth = true;
        }
        if (height < DisplayUtil.Height(context)) {
            is_NoFullScreenHeight = true;
        }
        setMeasuredDimension(width, height);

        if (is_firstdraw) {
            //获取每个item中心点
            cellDATAS = new ArrayList<>();
            for (int i = 0; i < treeDatas.size(); i++) {
                Tree tree = treeDatas.get(i);
                //计算当前圆左边有多少个圆，left_count
                int left_count = getleft_cells(tree.getId(), tree.getLevel());
                //计算当前圆上边有多少个圆，top_count
                int top_count = tree.getLevel();

                int level_count = getcountByLevel(tree.getLevel());
                //x:左边内边距+当前圆距离左边界距离+圆一半
                cellPoint point = new cellPoint(i,
                        (width - level_count * itemWidth - (level_count - 1) * cellHorizontalDistance) / 2
                                + (itemWidth + cellHorizontalDistance) * left_count
                                + itemWidth / 2
                        ,
                        cellStrokeWidth
                                + (itemHeight + cellVerticalDistance) * top_count
                                + itemHeight / 2, tree.getText(), false);
                cellDATAS.add(point);
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            int childW = childAt.getMeasuredWidth();
            int childH = childAt.getMeasuredHeight();

            cellPoint point = cellDATAS.get(i);
            int left = (int) (point.x - childW / 2);
            int top = (int) (point.y - childH / 2);
            int right = left + childW;
            int bottom = top + childH;
            childAt.layout(left, top, right, bottom);
        }

    }

    /**
     * 上一个被选中的下标
     */
    private int last_selected_index;

    private float last_move_x, last_move_y;

    /**
     * 按下是否选中某个圆
     */
    boolean is_down_selected = false;
    /**
     * 记录是否有滑动，处理点击事件
     */
    private boolean is_has_move = false;
    /**
     * 选中的子线绘制方式；0，向上；1，向下；
     */
    private int child_line_draw_way = -1;

    public void setChild_line_draw_way(int child_line_draw_way) {
        this.child_line_draw_way = child_line_draw_way;
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                is_has_move = false;

                last_move_x = event.getX();
                last_move_y = event.getY();
                //把之前的选中的设置不选中
                cellDATAS.get(last_selected_index).is_selected = false;
                //在设置选中
                for (int i = 0; i < cellDATAS.size(); i++) {
                    cellPoint cellPoint = cellDATAS.get(i);
                    float distance = GeometryUtil.getDistanceBetween2Points(new PointF(cellPoint.x, cellPoint.y), new PointF(last_move_x - translateX, last_move_y - translateY));
                    if (distance < itemHeight / 2) {
                        //按下已选中
                        is_down_selected = true;
                        last_selected_index = i;
                        break;
                    } else {
                        is_down_selected = false;
                    }
                }
                requestDisallowInterceptTouchEvent(false);
                break;
            case MotionEvent.ACTION_MOVE:
                float move_x = event.getX();
                float move_y = event.getY();

                float xx = move_x - last_move_x;
                float yy = move_y - last_move_y;

                //有选中，而且达到滑动条件
                if (is_down_selected && (Math.abs(xx) > 8 || Math.abs(yy) > 8)) {
                    is_has_move = true;
                    cellPoint cellPoint = cellDATAS.get(last_selected_index);
                    cellPoint.x = cellPoint.x + xx;
                    cellPoint.y = cellPoint.y + yy;

                    last_move_x = move_x;
                    last_move_y = move_y;
                }
                if (is_down_selected) {
                    requestLayout();
                    invalidate();
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                float up_x = event.getX();
                float up_y = event.getY();
                if (is_down_selected) {
                    //抬起后响应选中的item
                    if (!is_has_move) {//按下抬起，过程中没有滑动
                        for (int i = 0; i < cellDATAS.size(); i++) {
                            cellPoint cellPoint = cellDATAS.get(i);
                            float distance = GeometryUtil.getDistanceBetween2Points(new PointF(cellPoint.x, cellPoint.y), new PointF(up_x - translateX, up_y - translateY));
                            if (distance < itemHeight / 2) {
                                cellPoint.is_selected = true;
                                if (itemClickListener != null) {
                                    itemClickListener.OnTreeItemClick(itemWidth, cellPoint.x + translateX, cellPoint.y + translateY);
                                }
                                ToastUtil.showToast(context, cellPoint.text, Toast.LENGTH_SHORT);
                                break;
                            }
                        }
                        invalidate();
                    }

                    is_down_selected = false;
                }
                break;
        }

        return gesture.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (is_firstdraw) {
            if (width > DisplayUtil.Width(context)) {
                minX = -(width - DisplayUtil.Width(context)) / 2;
            }
            if (height > DisplayUtil.Height(context)) {
                minY = -(height - DisplayUtil.Height(context) + SystemAppUtils.getStatusHeight(context)) / 2;
            }
            maxX = (width - DisplayUtil.Width(context)) / 2;
            maxY = (height - DisplayUtil.Height(context) + SystemAppUtils.getStatusHeight(context)) / 2;

            maxX = maxX < 0 ? 0 : maxX;
            maxY = maxY < 0 ? 0 : maxY;

            this.translateX = maxX;
            this.translateY = maxY;
            is_firstdraw = false;
        }
        canvas.translate(getTranslateX(), getTranslateY());

        for (int i = 0; i < cellDATAS.size(); i++) {
            cellPoint point = cellDATAS.get(i);
            //画连线(当前圆 和 子结点圆相连线)
            List<cellPoint> childpoints = getChildTreesById(i);  //获取所有的子节点圆
            for (int c = 0; c < childpoints.size(); c++) {
                cellPoint child_point = childpoints.get(c);
                mPaint.setStrokeWidth(cellStrokeWidth);
                mPaint.setColor(getResources().getColor(R.color.green));
                draw_nocross_ciclecenter_lines(canvas, point, child_point, mPaint);
            }

        }

        //对选中的点，处理菜单逻辑
        cellPoint selected_point = cellDATAS.get(last_selected_index);
        if (selected_point.is_selected) {
            Tree tree = treeDatas.get(selected_point.pos);
            if (tree == null) {
                return;
            }
            if (child_line_draw_way == 0) {
                //一层一层向上找圆，绘制两者连线
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
            } else if (child_line_draw_way == 1) {
                //一层一层向下找子子子..结点，绘制两者连线
                List<Tree> downchildtrees = getDrawDownChild(tree, canvas);
                for (int i = 0; i < downchildtrees.size(); i++) {
                    Tree childtree = downchildtrees.get(i);
                    mPaint.setColor(Color.RED);
                    draw_nocross_ciclecenter_lines(canvas, cellDATAS.get(childtree.getParent().getId()), cellDATAS.get(childtree.getId()), mPaint);
                }
            }
        }
    }

    private List<Tree> getDrawDownChild(Tree tree, Canvas canvas) {
        List<Tree> AllChildTrees = new ArrayList<>();
        //第一层循环得到的结点，
        Tree cache_tree;
        //一层一层向下找圆，绘制两者连线
        for (int i = 0; i < treeDatas.size(); i++) {
            Tree tree1 = treeDatas.get(i);
            cache_tree = treeDatas.get(i);
            //如果是顶层，跳过；
            if (tree1.getParent() == null) {
                continue;
            }

            for (int k = 0; k < getMaxLevel(); k++) {
                //如果找到一个 父结点为顶层的，则终止当前循环；
                if (tree1.getParent() == null) {
                    break;
                }
                int p_id = tree1.getPid();
                if (p_id == tree.getId()) {
                    AllChildTrees.add(cache_tree);
                    break;
                }
                tree1 = tree1.getParent();
            }
        }
        return AllChildTrees;
    }

    /**
     * item单击时间接口
     */
    public interface OnTreeItemClickListener {
        void OnTreeItemClick(float itemWidth, float x, float y);
    }

    public OnTreeItemClickListener itemClickListener;

    public void setOnTreeItemClickListener(OnTreeItemClickListener listener) {
        this.itemClickListener = listener;
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
        boolean is_direction_bottom = p2.y > p1.y ? true : false;
        //当前圆圆心
        PointF start_point, end_point;

        //直线的斜率
        double tan = (p1.y - p2.y) / (p1.x - p2.x);

        //下个圆圆心与当前圆圆心连线直线 与 当前圆的交点
        PointF[] rectF1 = GeometryUtil.getIntersectionPoints(new PointF(p1.x, p1.y), itemHeight / 2, 1 / tan, is_direction_bottom);
        if (tan > 0) {
            start_point = rectF1[1];
        } else {
            start_point = rectF1[0];
        }
        //下个圆圆心与当前圆圆心连线直线 与 下个圆的交点
        PointF[] rectF2 = GeometryUtil.getIntersectionPoints(new PointF(p2.x, p2.y), itemHeight / 2, 1 / tan, is_direction_bottom);
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
