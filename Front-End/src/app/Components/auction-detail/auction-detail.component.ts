import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuctionService } from 'src/app/Services/Auction/auction.service';
import { AuctionDetails } from 'src/app/models/AuctionDetails';
import { Bid } from 'src/app/models/Bid';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { forkJoin } from 'rxjs';

@Component({
    selector: 'app-auction-detail',
    templateUrl: './auction-detail.component.html',
    styleUrls: ['./auction-detail.component.css'],
    standalone: false
})
export class AuctionDetailComponent {
  auctionId!: number;
  auction!: AuctionDetails;
  userId?: number;
  myForm!: FormGroup;
  currentPrice!: number;
  auctionOver: boolean = false; // new property

  constructor(private route: ActivatedRoute, private auctionService: AuctionService, private router: Router) { }

  ngOnInit() {
    this.myForm = new FormGroup({
      price: new FormControl(null, [Validators.required, Validators.min(1)]), // Ensure price is valid
    });

    this.auctionId = Number(this.route.snapshot.paramMap.get('id'));

    const userId = localStorage.getItem('userId');
    this.userId = userId ? Number(userId) : undefined;

    // Retrieve the auction details and determine if it is over
    this.auctionService.getAuction(this.auctionId).subscribe(auction => {
      this.auction = auction;
      this.auctionOver = new Date(this.auction.endDate) < new Date();
    });

    // Also retrieve the bids and calculate the current price.
    forkJoin([this.auctionService.getAuction(this.auctionId), this.auctionService.getBids(this.auctionId)]).subscribe(result => {
      const auction = result[0];
      const bids = result[1];
      this.auction = auction;
      this.auctionOver = new Date(this.auction.endDate) < new Date();

      if (bids.length > 0) {
        this.currentPrice = Math.max(...bids.map(bid => bid.price));
      } else {
        this.currentPrice = this.auction.minPrice;
      }
    });
  }

  addBid(price: number) {
    // Prevent bid submission if the auction is over.
    if (this.auctionOver) {
      alert('Auction is over. You cannot place a bid.');
      return;
    }
    // Prevent the same user who placed the auction from bidding.
    if (this.userId?.toString() === this.auction.id) {
      alert("You cannot bid on your own auction.");
      return;
    }

    // Ensure the bid is higher than the current price or the minimum price.
    if (price <= this.currentPrice) {
      alert(`Your bid must be higher than the current price of ${this.currentPrice} TND.`);
      return;
    }

    // Proceed to place the bid
    if (this.userId) {
      const bid: Bid = {
        userId: this.userId,
        price: price
      };
      this.auctionService.addBid(this.auctionId, bid).subscribe(id => {
        console.log(id);
        alert("Bid succeeded");
        this.router.navigate(['/Auctions']);
      });
    }
  }

  onSubmit() {
    const bidPrice = this.myForm.value.price;
    // Check for auction expiration before allowing the bid.
    if (this.auctionOver) {
      alert("Auction is over. You cannot place a bid.");
      return;
    }

    // Submit the bid if all conditions are met
    this.addBid(bidPrice);
  }
}
