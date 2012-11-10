
// Template version 1.1
// Java generated by Techne
// Keep in mind that you still need to fill in some blanks
// - ZeuX





package net.minecraft.src;

public class ModelPlesiosaur extends ModelDinosaurs
{
      public ModelPlesiosaur()
      {
        Body = new ModelRenderer(this, 0, 0);
        Body.addBox(-4F, -4F, -4F, 8, 6, 8);
        Body.setRotationPoint(0F, 20F, 0F);
        Body.rotateAngleX = 0F;
        Body.rotateAngleY = 0F;
        Body.rotateAngleZ = 0F;
        Body.mirror = false;
        Neck1 = new ModelRenderer(this, 20, 23);
        Neck1.addBox(-3F, 0F, -4F, 6, 5, 4);
        Neck1.setRotationPoint(0F, 17F, 0F);
        Neck1.rotateAngleX = -0.99446F;
        Neck1.rotateAngleY = 0F;
        Neck1.rotateAngleZ = 0F;
        Neck1.mirror = false;
        tail3 = new ModelRenderer(this, 24, 0);
        tail3.addBox(-1F, -1F, 0F, 2, 2, 6);
        tail3.setRotationPoint(0F, 21F, 11F);
        tail3.rotateAngleX = -0.18081F;
        tail3.rotateAngleY = 0F;
        tail3.rotateAngleZ = 0F;
        tail3.mirror = false;
        tail2 = new ModelRenderer(this, 18, 14);
        tail2.addBox(-2F, -2F, 0F, 4, 3, 6);
        tail2.setRotationPoint(0F, 20F, 5F);
        tail2.rotateAngleX = -0.27122F;
        tail2.rotateAngleY = 0F;
        tail2.rotateAngleZ = 0F;
        tail2.mirror = false;
        tail1 = new ModelRenderer(this, 0, 14);
        tail1.addBox(-3F, -5F, 2F, 6, 5, 3);
        tail1.setRotationPoint(0F, 20F, 0F);
        tail1.rotateAngleX = -0.45203F;
        tail1.rotateAngleY = 0F;
        tail1.rotateAngleZ = 0F;
        tail1.mirror = false;
        
        //Waving Tail New Codes:
        
        /*tail3 = new ModelRenderer(this, 24, 0);
        tail3.addBox(-1F, -1F, 0F, 2, 2, 6);
        tail3.setRotationPoint(0F, 19F, 11F);
        tail3.rotateAngleX = 0F;
        tail3.rotateAngleY = 0F;
        tail3.rotateAngleZ = 0F;
        tail3.mirror = false;
        
        tail2 = new ModelRenderer(this, 18, 14);
        tail2.addBox(-2F, -2F, 0F, 4, 3, 6);
        tail2.setRotationPoint(0F, 19F, 5F);
        tail2.rotateAngleX = 0F;
        tail2.rotateAngleY = 0F;
        tail2.rotateAngleZ = 0F;
        tail2.mirror = false;
        
        tail1 = new ModelRenderer(this, 0, 14);
        tail1.addBox(-3F, -5F, 2F, 6, 5, 3);
        tail1.setRotationPoint(0F, 21F, 0F);
        tail1.rotateAngleX = 0F;
        tail1.rotateAngleY = 0F;
        tail1.rotateAngleZ = 0F;
        tail1.mirror = false;*/
        //tail1.addChild(tail2);
        //tail2.addChild(tail3);
        Neck2 = new ModelRenderer(this, 47, 23);
        Neck2.addBox(-2F, -2F, -6F, 4, 4, 5);
        Neck2.setRotationPoint(0F, 16F, -4F);
        Neck2.rotateAngleX = -0.88974F;
        Neck2.rotateAngleY = 0F;
        Neck2.rotateAngleZ = 0F;
        Neck2.mirror = false;
        Neck3 = new ModelRenderer(this, 35, 3);
        Neck3.addBox(-1F, -2F, -5F, 2, 3, 5);
        Neck3.setRotationPoint(0F, 12.73333F, -8F);
        Neck3.rotateAngleX = -0.58764F;
        Neck3.rotateAngleY = 0F;
        Neck3.rotateAngleZ = 0F;
        Neck3.mirror = false;
        Neck4 = new ModelRenderer(this, 35, 3);
        Neck4.addBox(-1F, -2F, -5F, 2, 3, 5);
        Neck4.setRotationPoint(0F, 10F, -11F);
        Neck4.rotateAngleX = -0.13561F;
        Neck4.rotateAngleY = 0F;
        Neck4.rotateAngleZ = 0F;
        Neck4.mirror = false;
        head = new ModelRenderer(this, 0, 22);
        head.addBox(-2F, -2F, -6F, 4, 4, 6);
        head.setRotationPoint(0F, 9F, -15F);
        head.rotateAngleX = 0.49723F;
        head.rotateAngleY = 0F;
        head.rotateAngleZ = 0F;
        head.mirror = false;
        right_arm = new ModelRenderer(this, 44, 13);
        right_arm.addBox(0F, 0F, 0F, 6, 1, 4);
        right_arm.setRotationPoint(-3F, 21F, -3F);
        right_arm.rotateAngleX = -0.5236F;
        right_arm.rotateAngleY = -2.35619F;
        right_arm.rotateAngleZ = 0F;
        right_arm.mirror = false;
        left_arm = new ModelRenderer(this, 44, 18);
        left_arm.addBox(0F, 0F, -4F, 6, 1, 4);
        left_arm.setRotationPoint(3F, 21F, -3F);
        left_arm.rotateAngleX = 0.5236F;
        left_arm.rotateAngleY = -0.7854F;
        left_arm.rotateAngleZ = 0F;
        left_arm.mirror = true;
        right_leg = new ModelRenderer(this, 48, 0);
        right_leg.addBox(0F, 0F, 0F, 5, 1, 3);
        right_leg.setRotationPoint(-3F, 21F, 4F);
        right_leg.rotateAngleX = -0.5236F;
        right_leg.rotateAngleY = -2.0944F;
        right_leg.rotateAngleZ = 0F;
        right_leg.mirror = false;
        left_leg = new ModelRenderer(this, 48, 4);
        left_leg.addBox(0F, 0F, -3F, 5, 1, 3);
        left_leg.setRotationPoint(3F, 21F, 4F);
        left_leg.rotateAngleX = 0.5236F;
        left_leg.rotateAngleY = -1.0472F;
        left_leg.rotateAngleZ = 0F;
        left_leg.mirror = false;
      }

      public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
      {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5,((EntityDinosaurce)entity).isModelized());
        Body.render(f5);
        Neck1.render(f5);
        tail3.render(f5);
        tail2.render(f5);
        tail1.render(f5);
        Neck2.render(f5);
        Neck3.render(f5);
        Neck4.render(f5);
        head.render(f5);
        right_arm.render(f5);
        left_arm.render(f5);
        right_leg.render(f5);
        left_leg.render(f5);
      }
      public boolean WaveTail(float TargetAngle,boolean ClockDirection,int step){
    	  boolean result1,result2,result3=false;
    	  float ActionTargetAngle=TargetAngle;
    	  if (TargetAngle<0)return false;
    	  if (step<=0) return false;
    	  if (ClockDirection){
    		  //Clockwise
    		  if (tail1.rotateAngleY<ActionTargetAngle) tail1.rotateAngleY+=(ActionTargetAngle/step);
    		  else tail1.rotateAngleY=ActionTargetAngle;
    		  result1=(tail1.rotateAngleY>=ActionTargetAngle);
    		  ActionTargetAngle+=TargetAngle;
    		  if (!result1){
	    		  tail2.rotationPointX=(float) ((tail2.rotationPointX-tail1.rotationPointX)*Math.cos(-(ActionTargetAngle/step))-(tail2.rotationPointZ-tail1.rotationPointZ)*Math.sin(-(ActionTargetAngle/step))+tail1.rotationPointX);
	    		  tail2.rotationPointZ=(float) ((tail2.rotationPointX-tail1.rotationPointX)*Math.sin(-(ActionTargetAngle/step))+(tail2.rotationPointZ-tail1.rotationPointZ)*Math.cos(-(ActionTargetAngle/step))+tail1.rotationPointZ);
    		  }
    		  if (tail2.rotateAngleY<ActionTargetAngle) tail2.rotateAngleY+=(ActionTargetAngle/step);
    		  else tail2.rotateAngleY=ActionTargetAngle;
    		  result2=(tail2.rotateAngleY>=ActionTargetAngle);
    		  ActionTargetAngle+=TargetAngle;
    		  if (!result2){
	    		  tail3.rotationPointX=(float) ((tail3.rotationPointX-tail2.rotationPointX)*Math.cos(-(ActionTargetAngle/step))-(tail3.rotationPointZ-tail2.rotationPointZ)*Math.sin(-(ActionTargetAngle/step))+tail2.rotationPointX);
	    		  tail3.rotationPointZ=(float) ((tail3.rotationPointX-tail2.rotationPointX)*Math.sin(-(ActionTargetAngle/step))+(tail3.rotationPointZ-tail2.rotationPointZ)*Math.cos(-(ActionTargetAngle/step))+tail2.rotationPointZ);
    		  }
    		  if (tail3.rotateAngleY<ActionTargetAngle) tail3.rotateAngleY+=(ActionTargetAngle/step);
    		  else tail3.rotateAngleY=ActionTargetAngle;
    		  result3=(tail3.rotateAngleY>=ActionTargetAngle);
    	  }else{
    		  ActionTargetAngle=-ActionTargetAngle;
    		  //Anti-Clockwise
    		  if (tail1.rotateAngleY>ActionTargetAngle) tail1.rotateAngleY+=(ActionTargetAngle/step);
    		  else tail1.rotateAngleY=ActionTargetAngle;
    		  result1=(tail1.rotateAngleY<=ActionTargetAngle);
    		  ActionTargetAngle-=TargetAngle;
    		  if (!result1){
	    		  tail2.rotationPointX=(float) ((tail2.rotationPointX-tail1.rotationPointX)*Math.cos(-(ActionTargetAngle/step))-(tail2.rotationPointZ-tail1.rotationPointZ)*Math.sin(-(ActionTargetAngle/step))+tail1.rotationPointX);
	    		  tail2.rotationPointZ=(float) ((tail2.rotationPointX-tail1.rotationPointX)*Math.sin(-(ActionTargetAngle/step))+(tail2.rotationPointZ-tail1.rotationPointZ)*Math.cos(-(ActionTargetAngle/step))+tail1.rotationPointZ);
    		  }
    		  if (tail2.rotateAngleY>ActionTargetAngle) tail2.rotateAngleY+=(ActionTargetAngle/step);
    		  else tail2.rotateAngleY=ActionTargetAngle;
    		  result2=(tail2.rotateAngleY<=ActionTargetAngle);
    		  ActionTargetAngle-=TargetAngle;
    		  if (!result2){
	    		  tail3.rotationPointX=(float) ((tail3.rotationPointX-tail2.rotationPointX)*Math.cos(-(ActionTargetAngle/step))-(tail3.rotationPointZ-tail2.rotationPointZ)*Math.sin(-(ActionTargetAngle/step))+tail2.rotationPointX);
	    		  tail3.rotationPointZ=(float) ((tail3.rotationPointX-tail2.rotationPointX)*Math.sin(-(ActionTargetAngle/step))+(tail3.rotationPointZ-tail2.rotationPointZ)*Math.cos(-(ActionTargetAngle/step))+tail2.rotationPointZ);
    		  }
    		  if (tail3.rotateAngleY>ActionTargetAngle) tail3.rotateAngleY+=(ActionTargetAngle/step);
    		  else tail3.rotateAngleY=ActionTargetAngle;
    		  result3=(tail3.rotateAngleY<=ActionTargetAngle);
    	  }
    	  return (result1 && result2 && result3);
      }
      public void ReturnTail(){
    	  tail1.rotateAngleY=tail2.rotateAngleY=tail3.rotateAngleY=0F;
    	  tail1.setRotationPoint(0F, 21F, 0F);
    	  tail2.setRotationPoint(0F, 19F, 5F);
    	  tail3.setRotationPoint(0F, 19F, 11F);
      }
      public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5)
      {
        

      }
      /*public boolean HeadDown(){
    	  return true;
      }
      public boolean HeadRaise(){
    	  return true;
      }
      public boolean HeadNormal(){
    	  return true;
      }*/
      public void setLivingAnimations(EntityLiving entityliving, float f, float f1, float f2)
      {
    	  if (((EntityDinosaurce)entityliving).isModelized()) return;
    	  EntityPlesiosaur MainEntity=(EntityPlesiosaur)entityliving;
    	  final int STEPS=16+MainEntity.getDinoAge();
    	  if (MainEntity.riddenByEntity==null || MainEntity.isOnSurface()) PoseSurface(STEPS);
    	  else PoseDive(STEPS);
      }
      public boolean PoseDive(int steps){
    	  boolean result=true;
    	  if (Neck1.rotateAngleX<-0.453F) {
    		  Neck1.rotateAngleX+=((0.994F-0.453F)/steps);
    		  result&=false;
    	  }
    	  else Neck1.rotateAngleX=-0.453F;
    	  
    	  if (Neck2.rotateAngleX<-0.174F){
    		  Neck2.rotateAngleX+=((0.890F-0.174F)/steps);
    		  result&=false;
    	  }
    	  else Neck2.rotateAngleX=-0.174F;
    	  
    	  if (Neck2.rotationPointY<18F){
    		  Neck2.rotationPointY+=((18F-16F)/steps);
    		  result&=false;
    	  }else Neck2.rotationPointY=18F;
    	  
    	  if (Neck2.rotationPointZ<-3F){
    		  Neck2.rotationPointZ+=((4F-3F)/steps);
    		  result&=false;
    	  }else Neck2.rotationPointZ=-3F;
    	  
    	  if (Neck3.rotateAngleX<-0.116F){
    		  Neck3.rotateAngleX+=((0.588F-0.116F)/steps);
    		  result&=false;
    	  }else Neck3.rotateAngleX=-0.116F;
    	  
    	  if (Neck3.rotationPointY<17.7F){
    		  Neck3.rotationPointY+=((17.7F-12.7F)/steps);
    		  result&=false;
    	  }else Neck3.rotationPointY=17.7F;
    	  
    	  if (Neck3.rotationPointZ>-9F){
    		  Neck3.rotationPointZ-=((9F-8F)/steps);
    		  result&=false;
    	  }else Neck3.rotationPointZ=-9F;
    	  
    	  if (Neck4.rotateAngleX<-0.013F){
    		  Neck4.rotateAngleX+=((0.136F-0.013F)/steps);
    		  result&=false;
    	  }else Neck4.rotateAngleX=-0.013F;
    	  
    	  if (Neck4.rotationPointY<17F){
    		  Neck4.rotationPointY+=((17F-10F)/steps);
    		  result&=false;
    	  }else Neck4.rotationPointY=17F;
    	  
    	  if (Neck4.rotationPointZ>-13F){
    		  Neck4.rotationPointZ-=((13F-11F)/steps);
    		  result&=false;
    	  }else Neck4.rotationPointZ=-13F;
    	  
    	  if (head.rotateAngleX>0.009F){
    		  head.rotateAngleX-=((0.497F-0.009F)/steps);
    		  result&=false;
    	  }else head.rotateAngleX=0.009F;
    	  
    	  if (head.rotationPointY<16F){
    		  head.rotationPointY+=((16F-9F)/steps);
    		  result&=false;
    	  }else head.rotationPointY=16F;
    	  
    	  if (head.rotationPointZ>-18F){
    		  head.rotationPointZ-=((18F-15F)/steps);
    		  result&=false;
    	  }else head.rotationPointZ=-18F;
     	  return result;   	  
      }
      public boolean PoseSurface(int steps){
    	  boolean result=true;
    	  if (Neck1.rotateAngleX>-0.994F) {
    		  Neck1.rotateAngleX-=((0.994F-0.453F)/steps);
    		  result&=false;
    	  }
    	  else Neck1.rotateAngleX=-0.994F;
    	  
    	  if (Neck2.rotateAngleX>-0.890F){
    		  Neck2.rotateAngleX-=((0.890F-0.174F)/steps);
    		  result&=false;
    	  }
    	  else Neck2.rotateAngleX=-0.890F;
    	  
    	  if (Neck2.rotationPointY>16F){
    		  Neck2.rotationPointY-=((18F-16F)/steps);
    		  result&=false;
    	  }else Neck2.rotationPointY=16F;
    	  
    	  if (Neck2.rotationPointZ>-4F){
    		  Neck2.rotationPointZ-=((4F-3F)/steps);
    		  result&=false;
    	  }else Neck2.rotationPointZ=-4F;
    	  
    	  if (Neck3.rotateAngleX>-0.588F){
    		  Neck3.rotateAngleX-=((0.588F-0.116F)/steps);
    		  result&=false;
    	  }else Neck3.rotateAngleX=-0.588F;
    	  
    	  if (Neck3.rotationPointY>12.7F){
    		  Neck3.rotationPointY-=((17.7F-12.7F)/steps);
    		  result&=false;
    	  }else Neck3.rotationPointY=12.7F;
    	  
    	  if (Neck3.rotationPointZ<-8F){
    		  Neck3.rotationPointZ+=((9F-8F)/steps);
    		  result&=false;
    	  }else Neck3.rotationPointZ=-8F;
    	  
    	  if (Neck4.rotateAngleX>-0.136F){
    		  Neck4.rotateAngleX-=((0.136F-0.013F)/steps);
    		  result&=false;
    	  }else Neck4.rotateAngleX=-0.136F;
    	  
    	  if (Neck4.rotationPointY>10F){
    		  Neck4.rotationPointY-=((17F-10F)/steps);
    		  result&=false;
    	  }else Neck4.rotationPointY=10F;
    	  
    	  if (Neck4.rotationPointZ<-11F){
    		  Neck4.rotationPointZ+=((13F-11F)/steps);
    		  result&=false;
    	  }else Neck4.rotationPointZ=-11F;
    	  
    	  if (head.rotateAngleX<0.497F){
    		  head.rotateAngleX+=((0.497F-0.009F)/steps);
    		  result&=false;
    	  }else head.rotateAngleX=0.497F;
    	  
    	  if (head.rotationPointY>9F){
    		  head.rotationPointY-=((16F-9F)/steps);
    		  result&=false;
    	  }else head.rotationPointY=9F;
    	  
    	  if (head.rotationPointZ<-15F){
    		  head.rotationPointZ+=((18F-15F)/steps);
    		  result&=false;
    	  }else head.rotationPointZ=-15F;
     	  return result;
      }
      //fields
        ModelRenderer Body;
        ModelRenderer Neck1;
        ModelRenderer tail3;
        ModelRenderer tail2;
        ModelRenderer tail1;
        ModelRenderer Neck2;
        ModelRenderer Neck3;
        ModelRenderer Neck4;
        ModelRenderer head;
        ModelRenderer right_arm;
        ModelRenderer left_arm;
        ModelRenderer right_leg;
        ModelRenderer left_leg;
        public boolean LandFlag=false;
		@Override
		protected void setRotationAngles(float f, float f1, float f2, float f3,
				float f4, float f5, boolean modelized) {
			if (modelized) return;
			//super.setRotationAngles(f, f1, f2, f3, f4, f5);
			
	    	//Head Yaw
	    	  //head.rotateAngleY = -f3 / 57.29578F;  
	    	  
	    	  
		       right_arm.rotateAngleY = (float) (MathHelper.cos(f / (1.919107651F * 0.5F )) * 0.785398163397448 * f1 + -2.35619449019234) ;
		       right_leg.rotateAngleY = (float) (MathHelper.cos(f / (1.919107651F * 0.5F )) * 0.785398163397448 * f1 + -2.0943951023932) ;
	    	  if (LandFlag){

	    	       left_arm.rotateAngleY = (float) (MathHelper.cos(f / (1.919107651F * 0.5F )) * 0.785398163397448 * f1 + -0.785398163397448) ;

	    	       left_leg.rotateAngleY = (float) (MathHelper.cos(f / (1.919107651F * 0.5F )) * 0.785398163397448 * f1 + -1.0471975511966) ;
	    	  }else{

	   	       left_arm.rotateAngleY = (float) (MathHelper.cos(f / (1.919107651F * 0.5F )) * -0.785398163397448 * f1 + -0.785398163397448) ;

	   	       left_leg.rotateAngleY = (float) (MathHelper.cos(f / (1.919107651F * 0.5F )) * -0.785398163397448 * f1 + -1.0471975511966) ;
	    	  }
			
		}
}
