import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams } from 'ionic-angular';

/**
 * Generated class for the DetailPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-detail',
  templateUrl: 'detail.html',
})
export class DetailPage {
public image;
public propertyType;
public value;
public bedrooms;
public bathrooms;
public area;
public description;
public location;
public currentLat;
public currentLong;

  constructor(public navCtrl: NavController, public navParams: NavParams) {
  	this.image=navParams.get('image');
  	this.propertyType=navParams.get('propertyType');
  	this.value=navParams.get('value');
  	this.bedrooms=navParams.get('bedrooms');
  	this.bathrooms=navParams.get('bathrooms');
  	this.area=navParams.get('area');
  	this.description=navParams.get('description');
  	this.location=navParams.get('location');
  }

  ionViewDidLoad() {
    console.log('ionViewDidLoad DetailPage');
  }

}
