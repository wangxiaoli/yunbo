package com.yunbo.frame;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.FrameLayout;
import android.widget.TextView;

public class KeywordsView extends FrameLayout implements OnGlobalLayoutListener {
	
	public static final int IDX_X = 0;
	public static final int IDX_Y = 1;
	public static final int IDX_TXT_LENGTH = 2;
	public static final int IDX_DIS_Y = 3;
	
	/** 从外至内的动??*/
	public static final int ANIMATION_IN = 1;
	/** 从内至外的动??*/
	public static final int ANIMATION_OUT = 2;
	
	/*** 位移的动画类??从外围移动到坐标?? */
	public static final int OUTSIDE_TO_LOCATION = 1;
	/** 位移的动画类??从坐标点移动的外??*/
	public static final int LOCATION_TO_OUTSIDE = 2;
	/** 位移的动画类??从中心点移动到坐标点 */	
	public static final int CENTER_TO_LOCATION = 3;
	/** 位移的动画类??从坐标点移动到中心点 */
	public static final int LOCATION_TO_CENTER = 4;
	
	public static final long ANIM_DURATION = 800l;
	public static final int MAX = 15;
	public static final int TEXT_SIZE_MAX = 20;
	public static final int TEXT_SIZE_MIN = 16;
	
	private OnClickListener itemClickListener;
	private static Interpolator interpolator;
	private static AlphaAnimation animAlpha2Opaque;
	private static AlphaAnimation animAlpha2Transparent;
	private static ScaleAnimation animScaleLarge2Normal, animScaleNormal2Large, animScaleZero2Normal,animScaleNormal2Zero;
	
	/** 存储显示的关键字 */
	private Vector<String> vecKeywords;
	private int width , height;

	/**
	 * go2Show()中被赋??为true,标识????人员触发其开始动画显??
	 * 本标识的作用是防止在填充keywrods为完成的过程中获取到width和height后提前启动动??
	 * 在show()方法中其被赋值为false.
	 * 真正能够动画显示的另????要条??width和height不为0.
	 */
	private boolean enableShow;
	private Random random;

	/** 
     * @see ANIMATION_IN 
     * @see ANIMATION_OUT 
     * @see OUTSIDE_TO_LOCATION 
     * @see LOCATION_TO_OUTSIDE 
     * @see LOCATION_TO_CENTER 
     * @see CENTER_TO_LOCATION 
     * */  
    private int txtAnimINType, txtAnimOutType;  
    /** ????????启动动画显示的时间??     */  
    private long lastStartAnimationTime;
    /**  动画运行时间     */
    private long animDuration ;
    
    public KeywordsView(Context context){
    	super(context);
    	init();
    }
    
    public KeywordsView(Context context, AttributeSet attrs){
    	super(context,attrs);
    	init();
    }
    
	public KeywordsView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	private void init(){
		lastStartAnimationTime = 0l;
		animDuration = ANIM_DURATION;
		random = new Random();
		vecKeywords = new Vector<String>(MAX);
		getViewTreeObserver().addOnGlobalLayoutListener(this);
		interpolator = AnimationUtils.loadInterpolator(getContext(), android.R.anim.decelerate_interpolator);
		animAlpha2Opaque = new AlphaAnimation(0.0f, 1.0f);
		animAlpha2Transparent = new AlphaAnimation(1.0f, 0.0f);
		animScaleLarge2Normal = new ScaleAnimation(2, 1, 2, 1);
		animScaleNormal2Large = new ScaleAnimation(1, 2, 1, 2);
		animScaleZero2Normal = new ScaleAnimation(0, 1, 0 ,1);
		animScaleNormal2Zero = new ScaleAnimation(1, 0, 1, 0);
	}
	 
	public long getDuration(){
		return animDuration;
	}
	
	public void setDuration(long duration){
		animDuration = duration;
	}
	
	public boolean feedKeyword(String keyword){
		boolean result = false;
		if(vecKeywords.size()<MAX){
			result = vecKeywords.add(keyword);
		}
		return result;
	}
	/** 
     * ????动画显示??br/> 
     * 之前已经存在的TextView将会显示????动画??br/> 
     *  
     * @return 正常显示动画返回true；反之为false。返回false原因如下??br/> 
     *         1.时间上不允许，受lastStartAnimationTime的制约；<br/> 
     *         2.未获取到width和height的????br/> 
     */  
	public boolean go2Shwo(int animType){
		if(System.currentTimeMillis() - lastStartAnimationTime > animDuration){
			enableShow = true;
			if(animType == ANIMATION_IN){
				txtAnimINType = OUTSIDE_TO_LOCATION;
				txtAnimOutType = LOCATION_TO_CENTER;
			}else if(animType == ANIMATION_OUT){
				txtAnimINType = CENTER_TO_LOCATION;
				txtAnimOutType = LOCATION_TO_OUTSIDE;
			}
			disapper();
			boolean result = show();
			return result;
		}
		return false;
	}
	
	private void disapper() {
		int size = getChildCount();
//		Log.e("---","disapper()-size="+size);
		for (int i = size - 1; i >= 0; i--) {
			final TextView txt = (TextView) getChildAt(i);
			if(txt.getVisibility() == View.GONE){
				removeView(txt);
				continue;
			}
			FrameLayout.LayoutParams layParams = (LayoutParams) txt.getLayoutParams();
			int[] xy = new int[]{layParams.leftMargin,layParams.topMargin,txt.getWidth()};
//			Log.e("---", "disapper()-layParams.leftMargin="+layParams.leftMargin+
//					";layParams.topMargin="+layParams.topMargin);
			AnimationSet animSet = getAnimationSet(xy,(width >> 1),(height >> 1),txtAnimOutType);
			txt.startAnimation(animSet);
			/************/
			animSet.setFillBefore(true);
			/************/
			animSet.setAnimationListener(new AnimationListener() {
				
				public void onAnimationStart(Animation animation) {
				}
				
				public void onAnimationRepeat(Animation animation) {
				}
				
				public void onAnimationEnd(Animation animation) {
					txt.setOnClickListener(null);
					txt.setClickable(true);
					txt.setVisibility(View.GONE);
				}
			});
		}
	}
	
	private boolean show() {
		if(width > 0 && height > 0 && vecKeywords != null && vecKeywords.size() > 0 && enableShow){
//			Log.e("---","show()-width="+width+",height="+height);
			enableShow = false;
			lastStartAnimationTime = System.currentTimeMillis();
			int xCenter = width >> 1, yCenter = height >> 1;
//			Log.e("---","show()-xCenter="+xCenter+",yCenter="+yCenter);
			int size = vecKeywords.size();
			int xItem = width / size , yItem = height / size;
//			Log.e("---","show()-xItem="+xItem+",yItem="+yItem);
			LinkedList<Integer> listX = new LinkedList<Integer>(), listY = new LinkedList<Integer>();
			for (int i = 0; i < size; i++) {
				listX.add(i*xItem);
				listY.add(i*yItem + (yItem >> 2));
			}
			LinkedList<TextView> listTxtTop = new LinkedList<TextView>();
			LinkedList<TextView> listTxtBottom = new LinkedList<TextView>();
			for (int i = 0; i < size; i++) {
				String keyword = vecKeywords.get(i);
				//随机颜色
				//int ranColor =  random.nextInt(4);
				int color =  Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255));
				//随机位置,粗糙
				int xy[] =randomXY(random,listX,listY,xItem);
				//随机字体大小
				int txtSize = TEXT_SIZE_MIN ;//+ random.nextInt(TEXT_SIZE_MAX - TEXT_SIZE_MIN + 1);
				//实例化Textview
				final TextView txt = new TextView(getContext());
				txt.setOnClickListener(itemClickListener);
				txt.setText(keyword);
				txt.setTextColor(color);
				txt.setTextSize(TypedValue.COMPLEX_UNIT_SP,txtSize);
				txt.setShadowLayer(2, 2, 2, 0xff696969);
				txt.setGravity(Gravity.CENTER);
				txt.setEllipsize(TruncateAt.MIDDLE);
				txt.setSingleLine(true);
				txt.setEms(16);
				//获取文本长度
				Paint paint = txt.getPaint();
//				System.out.println("KKKKKKKKKKKKKKKKeyword = "+keyword);
				int strWidth = (int) Math.ceil(paint.measureText(keyword));
				xy[IDX_TXT_LENGTH] = strWidth;
				if(xy[IDX_X] + strWidth > width - (xItem >> 1)){
					int baseX = width - strWidth;
					xy[IDX_X] = baseX - xItem + random.nextInt(xItem>>1);
				}else if(xy[IDX_X] == 0){
					xy[IDX_X] = Math.max(random.nextInt(xItem), xItem / 3);
				}
				xy[IDX_DIS_Y] = Math.abs(xy[IDX_Y] - yCenter); 
				txt.setTag(xy);
				if(xy[IDX_Y]> yCenter){
					listTxtBottom.add(txt);
				}else{
					listTxtTop.add(txt);
				}
			}
			attch2Screen(listTxtTop,xCenter,yCenter,yItem);
			attch2Screen(listTxtBottom,xCenter,yCenter,xItem);
			return true;
		}
		return false;
	}

	private void attch2Screen(LinkedList<TextView> listTxt, int xCenter,
			int yCenter, int yItem) {
		int size = listTxt.size();
//		Log.e("---","attch2Screen()-size="+size);
		sortXYList(listTxt,size);
		int th=height/size;
		int tx=0;
		List<Integer>ys=new ArrayList<Integer>();
		for (int i = 0; i < size; i++) {
			ys.add(th*i+random.nextInt(th/2));
		}
		for (int i = 0; i < size; i++) {
			TextView txt = listTxt.get(i);
			int[] iXY = (int[]) txt.getTag();
			//第二次修??修正Y轴坐??
			int yDistance = iXY[IDX_Y] - yCenter;
			//对于????近中心的,其??不会大于yItem
			//对于可以????下降到中心点??则该值也是其应调整的大小
			int yMove = Math.abs(yDistance);
			inner:for (int k = i - 1; k >= 0; k--) {
				int[] kXY = (int[]) listTxt.get(k).getTag();
				int startX = kXY[IDX_X];
				int endX = startX + kXY[IDX_TXT_LENGTH];
                //y轴一中心点为分隔??在同????   
				if(yDistance * (kXY[IDX_Y] - yCenter) > 0){
					if(isXMixed(startX, endX, iXY[IDX_X], iXY[IDX_X] + iXY[IDX_TXT_LENGTH])){
						int tmpMove = Math.abs(iXY[IDX_Y] - kXY[IDX_Y]);
						if(tmpMove > yItem){
							yMove = tmpMove;
						}else if(yMove > 0){
							yMove = 0;
						}
						break inner;
					}
				}
			}
			if(yMove > yItem){
				int maxMove = yMove - yItem;
				int randomMove = random.nextInt(maxMove);
				int realMove = Math.max(randomMove, maxMove >> 1)*yDistance/Math.abs(yDistance);
				iXY[IDX_Y] = iXY[IDX_Y] - realMove;
				iXY[IDX_DIS_Y] = Math.abs(iXY[IDX_Y] - yCenter);
				//已经调整过的Y????在排??
				sortXYList(listTxt, i+1);
			}
			FrameLayout.LayoutParams layParams = new FrameLayout.LayoutParams(
					FrameLayout.LayoutParams.WRAP_CONTENT,FrameLayout.LayoutParams.WRAP_CONTENT);
			layParams.gravity = Gravity.LEFT | Gravity.TOP; 
			int x=random.nextInt((3)*width/4) ;//(width/2*i)%width;
			//x=i%3*width/4;
			 int y= ys.remove(random.nextInt(ys.size()));
			 while (Math.abs(th-x)<width/3 ) x=random.nextInt(3*width/4) ;
			 if (txt.getText().length()>6) {
				 x=random.nextInt(width/2) ;
			}
			 if (i%3==0) {
				x=0;
			}
			 th=x;
				 layParams.leftMargin =x;
			layParams.topMargin = y ;
			addView(txt,layParams);
			//动画
			AnimationSet animSet = getAnimationSet(iXY, xCenter, yCenter, txtAnimINType);
			txt.startAnimation(animSet);
		}
	}
	private AnimationSet getAnimationSet(int[] xy, int xCenter, int yCenter,
			int type) {
		AnimationSet animSet = new AnimationSet(true);
		animSet.setInterpolator(interpolator);
		if(type == OUTSIDE_TO_LOCATION){
			animSet.addAnimation(animAlpha2Opaque);
			animSet.addAnimation(animScaleLarge2Normal);
			TranslateAnimation translate = new TranslateAnimation(
					(xy[IDX_X] +(xy[IDX_TXT_LENGTH]>>1)-xCenter)<<1, 0, (xy[IDX_Y] -yCenter)<<1, 0);
			animSet.addAnimation(translate);
		}else if (type == LOCATION_TO_OUTSIDE){
			animSet.addAnimation(animAlpha2Transparent);
			animSet.addAnimation(animScaleNormal2Large);
			TranslateAnimation translate = new TranslateAnimation(
					0, (xy[IDX_X] +(xy[IDX_TXT_LENGTH]>>1)-xCenter)<<1, 0, (xy[IDX_Y] -yCenter)<<1);
			animSet.addAnimation(translate);
		}else if (type == LOCATION_TO_CENTER){
			animSet.addAnimation(animAlpha2Transparent);
			animSet.addAnimation(animScaleNormal2Zero);
			TranslateAnimation translate = new TranslateAnimation(
					0, xCenter - xy[IDX_X], 0, yCenter - xy[IDX_Y]);
			animSet.addAnimation(translate);
		}else if (type == CENTER_TO_LOCATION){
			animSet.addAnimation(animAlpha2Opaque);
			animSet.addAnimation(animScaleZero2Normal);
			TranslateAnimation tranlate = new TranslateAnimation(
					xCenter - xy[IDX_X], 0,yCenter - xy[IDX_Y],  0);
			animSet.addAnimation(tranlate);
		}
		animSet.setDuration(animDuration);
		animSet.setFillBefore(true);
		return animSet;
	}
	private void sortXYList(LinkedList<TextView> listTxt, int endIdx) {
		for (int i = 0; i < endIdx; i++) {
			for (int k = i+1; k < endIdx; k++) {
				if(((int[])listTxt.get(k).getTag())[IDX_DIS_Y] < ((int[])listTxt.get(i).getTag())[IDX_DIS_Y]){
					TextView iTmp = listTxt.get(i);
					TextView kTmp = listTxt.get(k);
					listTxt.set(i, kTmp);
					listTxt.set(k, iTmp);
				}
			}
		}
	}
	private boolean isXMixed(int startA, int endA, int startB, int endB) {
		boolean result = false;
		if(startB >= startA && startB <= endA){
			result = true;
		}else if (endB >= startA && endB <= endA){
			result = true;
		}else if (startA >= startB && startA <= endB){
			result = true;
		}else if (endA >= startB && endA <= endB){
			result =true;
		}
		return result;
	}
	private int[] randomXY(Random ran, LinkedList<Integer> listX,
			LinkedList<Integer> listY, int xItem) {
		int[] arr = new int[4];
		arr[IDX_X] = listX.remove(ran.nextInt(listX.size()));
		arr[IDX_Y] = listY.remove(ran.nextInt(listY.size()));
		//arr[IDX_X]=ran.nextInt(arr[IDX_X]);
		//arr[IDX_Y]=ran.nextInt(arr[IDX_Y]);
		return arr;
	}
	public void onGlobalLayout() {
		int tmpW = getWidth();
		int tmpH = getHeight();
//		Log.e("---","onGlobalLayout()--tmpW="+tmpW+",tmpH="+tmpH);
		if(width != tmpW || height != tmpH){
			width = tmpW;
			height =tmpH;
			show();
		}
	}
	public Vector<String> getKeywords(){
		return vecKeywords;
	}
	
	public void rubKeywords(){
		vecKeywords.clear();
	}
	
	public void rubAllViews(){
		removeAllViews();
	}
	
	public void setOnClickListener(OnClickListener listener){
		itemClickListener = listener;
	}
	
}