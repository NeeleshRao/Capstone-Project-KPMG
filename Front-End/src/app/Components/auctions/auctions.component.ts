import { Component } from '@angular/core';
import { AuctionService } from 'src/app/Services/Auction/auction.service';
import { AuctionDetails } from 'src/app/models/AuctionDetails';

@Component({
    selector: 'app-auctions',
    templateUrl: './auctions.component.html',
    styleUrls: ['./auctions.component.css'],
    standalone: false
})
export class AuctionsComponent {
  // auctions!: AuctionDetails[];
  allAuctions: AuctionDetails[] = [];
  auctions: AuctionDetails[] = [];

  constructor(private auctionService: AuctionService) {}

  ngOnInit() {
    this.auctionService.getAuctions().subscribe((auctions: AuctionDetails[]) => {
      this.allAuctions = auctions;
      this.auctions = [...auctions]; // clone
    });
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value.toLowerCase();

  if (filterValue !== "") {
    this.auctions = this.allAuctions.filter(item =>
      item.product.name.toLowerCase().includes(filterValue)
    );
  } else {
    this.auctions = [...this.allAuctions]; // reset to original
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

  sortAuctions(type: string) {
    switch (type) {
      case 'latest':
        this.auctions.sort((a, b) => new Date(b.endDate).getTime() - new Date(a.endDate).getTime());
        break;
      case 'nearest':
        this.auctions.sort((a, b) => new Date(a.endDate).getTime() - new Date(b.endDate).getTime());
        break;
      case 'lowestPrice':
        this.auctions.sort((a, b) => a.minPrice - b.minPrice);
        break;
      case 'highestPrice':
        this.auctions.sort((a, b) => b.minPrice - a.minPrice);
        break;
    }
  }

  
}
