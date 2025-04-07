import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuctionService } from 'src/app/Services/Auction/auction.service';
import { Auction } from 'src/app/models/Auction';
import { AuctionDetails } from 'src/app/models/AuctionDetails';

@Component({
    selector: 'app-add-auction',
    templateUrl: './add-auction.component.html',
    styleUrls: ['./add-auction.component.css'],
    standalone: false
})
export class AddAuctionComponent  implements OnInit {
  myForm!: FormGroup;
  constructor(private auctionService:AuctionService , private router:Router){}
  ngOnInit(): void {
    this.myForm = new FormGroup({
      name: new FormControl(null, Validators.required),
      category: new FormControl(null, Validators.required),
      // category: this.chooseCategory(new FormControl(null, Validators.required)),
    minPrice: new FormControl(null, Validators.required),
    endDate: new FormControl(null, Validators.required),
    auctionStatus: new FormControl(null, Validators.required),
    // prodImage: new FormControl(null, Validators.required)
  });
  console.log(this.myForm)
}

chooseCategory(type:string):string{
  switch(type) {
    case "vehicles":
      return "VEHICLES";
      break;
    case "home":
      return "HOME";
      break;
    case "hobbies":
      return "HOBBIES";
      break;
    case "clothing":
      return "CLOTHING";
      break;
    case "electronics":
      return "ELECTRONICS";
      break;
  }
  return "";
}
  onSubmit() {
    const newAuction : Auction ={
      product: {
        name: this.myForm.value.name,
        category: this.chooseCategory(this.myForm.value.category)
      },

      minPrice: this.myForm.value.minPrice,
      endDate: this.myForm.value.endDate,      
    };
    console.log(this.myForm.value.category)
    // productImage: this.myForm.value.prodImage;
    
    this.auctionService.saveDataA(Number(localStorage.getItem("userId")), newAuction).subscribe(()=> {
      // console.log(response);
      this.router.navigate(['/Auctions']);
      
    });
 
  }

}
