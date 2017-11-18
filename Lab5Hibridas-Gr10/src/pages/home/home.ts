import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams } from 'ionic-angular';
import { AngularFireList, AngularFireDatabase } from 'angularfire2/database';
import { Observable } from 'rxjs/Observable';

/**
 * Generated class for the HomePage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-home',
  templateUrl: 'home.html',
})
export class HomePage {

  private apartmentsCollection: AngularFireList<any>;
  apartments: Observable<any[]>;

  constructor(public navCtrl: NavController, public navParams: NavParams,public db: AngularFireDatabase) {
  	this.apartmentsCollection=this.db.list('apartments');
  	this.apartments=this.apartmentsCollection.valueChanges();
  }

  ionViewDidLoad() {
    console.log('ionViewDidLoad HomePage');
  }

  show(apartment){
  	this.navCtrl.push('DetailPage', {
  		image: apartment.image,
  		propertyType: apartment.propertyType,
  		value: apartment.value,
  		bedrooms: apartment.amountRooms,
  		bathrooms: apartment.bathRooms,
  		area: apartment.area,
  		description: apartment.description,
  		location: apartment.location
  	});
  }

}
