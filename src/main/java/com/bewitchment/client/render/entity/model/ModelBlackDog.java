package com.bewitchment.client.render.entity.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * blackdog - cybercat5555
 * Created using Tabula 5.1.0
 */
public class ModelBlackDog extends ModelBase {
	public ModelRenderer lArm01;
	public ModelRenderer body;
	public ModelRenderer lHindLeg01;
	public ModelRenderer rHindLeg01;
	public ModelRenderer rArm01;
	public ModelRenderer chest;
	public ModelRenderer tail01;
	public ModelRenderer neck;
	public ModelRenderer mane02;
	public ModelRenderer mane01;
	public ModelRenderer head;
	public ModelRenderer lEar;
	public ModelRenderer rEar;
	public ModelRenderer muzzle;
	public ModelRenderer lowerJaw;
	public ModelRenderer lEar02;
	public ModelRenderer rEar02;

	public ModelBlackDog() {
		this.textureWidth = 64;
		this.textureHeight = 64;
		this.neck = new ModelRenderer(this, 0, 32);
		this.neck.setRotationPoint(0.0F, -5.4F, 0.5F);
		this.neck.addBox(-2.5F, -2.5F, -4.0F, 5, 5, 4, 0.0F);
		this.setRotateAngle(neck, -1.5707963267948966F, 0.0F, 0.0F);
		this.lowerJaw = new ModelRenderer(this, 0, 43);
		this.lowerJaw.setRotationPoint(0.0F, 2.0F, -3.8F);
		this.lowerJaw.addBox(-1.5F, -0.4F, -3.0F, 3, 1, 3, 0.0F);
		this.rEar = new ModelRenderer(this, 16, 14);
		this.rEar.mirror = true;
		this.rEar.setRotationPoint(-2.0F, -2.0F, -2.0F);
		this.rEar.addBox(-2.0F, -1.0F, -1.0F, 2, 1, 2, 0.0F);
		this.setRotateAngle(rEar, 0.0F, 0.0F, 0.5462880558742251F);
		this.muzzle = new ModelRenderer(this, 0, 10);
		this.muzzle.setRotationPoint(0.0F, 0.7F, -3.9F);
		this.muzzle.addBox(-1.5F, -1.0F, -3.0F, 3, 2, 3, 0.0F);
		this.tail01 = new ModelRenderer(this, 9, 18);
		this.tail01.setRotationPoint(0.0F, 5.7F, 2.0F);
		this.tail01.addBox(-1.0F, 0.0F, -1.0F, 2, 9, 2, 0.0F);
		this.setRotateAngle(tail01, -0.8726646259971648F, 0.0F, 0.0F);
		this.lEar = new ModelRenderer(this, 16, 14);
		this.lEar.setRotationPoint(2.0F, -2.0F, -2.0F);
		this.lEar.addBox(0.0F, -1.0F, -1.0F, 2, 1, 2, 0.0F);
		this.setRotateAngle(lEar, 0.0F, 0.0F, -0.5462880558742251F);
		this.rHindLeg01 = new ModelRenderer(this, 0, 18);
		this.rHindLeg01.mirror = true;
		this.rHindLeg01.setRotationPoint(-1.5F, 16.0F, 6.0F);
		this.rHindLeg01.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F);
		this.lEar02 = new ModelRenderer(this, 39, 14);
		this.lEar02.setRotationPoint(1.9F, -0.9F, 0.0F);
		this.lEar02.addBox(-0.5F, -0.1F, -1.0F, 1, 3, 2, 0.0F);
		this.setRotateAngle(lEar02, 0.0F, 0.0F, 0.22759093446006054F);
		this.lArm01 = new ModelRenderer(this, 0, 18);
		this.lArm01.setRotationPoint(1.5F, 16.0F, -4.0F);
		this.lArm01.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F);
		this.mane02 = new ModelRenderer(this, 28, 48);
		this.mane02.setRotationPoint(0.0F, -1.0F, 2.7F);
		this.mane02.addBox(-3.5F, -1.0F, 0.0F, 7, 2, 7, 0.0F);
		this.setRotateAngle(mane02, -1.2915436464758039F, 0.0F, 0.0F);
		this.lHindLeg01 = new ModelRenderer(this, 0, 18);
		this.lHindLeg01.setRotationPoint(1.5F, 16.0F, 6.0F);
		this.lHindLeg01.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F);
		this.rArm01 = new ModelRenderer(this, 0, 18);
		this.rArm01.mirror = true;
		this.rArm01.setRotationPoint(-1.5F, 16.0F, -4.0F);
		this.rArm01.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F);
		this.rEar02 = new ModelRenderer(this, 39, 14);
		this.rEar02.mirror = true;
		this.rEar02.setRotationPoint(-1.9F, -0.9F, 0.0F);
		this.rEar02.addBox(-0.5F, -0.1F, -1.0F, 1, 3, 2, 0.0F);
		this.setRotateAngle(rEar02, 0.0F, 0.0F, -0.22759093446006054F);
		this.body = new ModelRenderer(this, 18, 14);
		this.body.setRotationPoint(0.0F, 14.0F, 1.0F);
		this.body.addBox(-3.0F, -2.0F, -3.0F, 6, 9, 6, 0.0F);
		this.setRotateAngle(body, 1.5707963267948966F, 0.0F, 0.0F);
		this.head = new ModelRenderer(this, 0, 0);
		this.head.setRotationPoint(0.0F, 0.0F, -2.9F);
		this.head.addBox(-3.0F, -3.0F, -4.0F, 6, 6, 4, 0.0F);
		this.mane01 = new ModelRenderer(this, 0, 48);
		this.mane01.setRotationPoint(0.0F, -1.8F, -3.0F);
		this.mane01.addBox(-3.0F, -1.0F, 0.0F, 6, 2, 7, 0.0F);
		this.setRotateAngle(mane01, 0.4363323129985824F, 0.0F, 0.0F);
		this.chest = new ModelRenderer(this, 21, 0);
		this.chest.setRotationPoint(0.0F, -4.0F, 0.0F);
		this.chest.addBox(-4.0F, -3.5F, -3.01F, 8, 6, 7, 0.0F);
		this.body.addChild(this.neck);
		this.head.addChild(this.lowerJaw);
		this.head.addChild(this.rEar);
		this.head.addChild(this.muzzle);
		this.body.addChild(this.tail01);
		this.head.addChild(this.lEar);
		this.lEar.addChild(this.lEar02);
		this.chest.addChild(this.mane02);
		this.rEar.addChild(this.rEar02);
		this.neck.addChild(this.head);
		this.neck.addChild(this.mane01);
		this.body.addChild(this.chest);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		this.rHindLeg01.render(f5);
		this.lArm01.render(f5);
		this.lHindLeg01.render(f5);
		this.rArm01.render(f5);
		this.body.render(f5);
	}

	/**
	 * This is a helper function from Tabula to set the rotation of model parts
	 */
	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}
