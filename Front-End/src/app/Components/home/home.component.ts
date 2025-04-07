import { Component } from '@angular/core';
import { AuctionService } from 'src/app/Services/Auction/auction.service';
import { AuctionDetails } from 'src/app/models/AuctionDetails';

@Component({
    selector: 'app-home',
    templateUrl: './home.component.html',
    styleUrls: ['./home.component.css'],
    standalone: false
})
export class HomeComponent {
  auctions!: AuctionDetails[];

  constructor(private auctionService: AuctionService) {}

  ngOnInit() {
    this.auctionService.getAuctions().subscribe((auctions: AuctionDetails[]) => {
      this.auctions = auctions;
      console.log(auctions);
    });
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    // console.log(filterValue);
    if(filterValue!=""){
      this.auctions = this.auctions.filter(item => item.product.name
        .toLowerCase().startsWith(filterValue
        .toLowerCase()));
    }
    console.log(filterValue);
    if(filterValue=="") {      
      this.ngOnInit();
    }
  }

  getImagePath(category : string) : string {
    switch(category) {
      case "VEHICLES":
        return "../../../assets/images/vehicles.png";
      case "HOME":
        return "../../../assets/images/homes.png";
      case "HOBBIES":
        return "../../../assets/images/hobbies.png";
      case "CLOTHING":
        return "../../../assets/images/clothing.png";
      case "ELECTRONICS":
        return "../../../assets/images/electronics.jpg";
      
    }
    return "";
  }
}
