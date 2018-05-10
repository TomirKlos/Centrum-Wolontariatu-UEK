import { Component, OnInit } from '@angular/core';
import { SearchService } from '../shared/search-service.service'

import { NguCarousel, NguCarouselStore } from '@ngu/carousel';
import { Subject } from 'rxjs/Subject';
import { MatAutocompleteModule } from '@angular/material';



@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  results: Object;
  searchTerm$ = new Subject<string>();
  

  constructor(private searchService: SearchService) {
    this.searchService.search(this.searchTerm$)
    .subscribe(results => {
      this.results = results;
    }); 
  }

  public carouselBanner: NguCarousel;
  public carouselBanerItems: Array<any>;
  public carouselBanerTitles: Array<any>;
  public bales;

  ngOnInit() {
    this.carouselBanerItems = ["https://sheikalthaf.github.io/ngx-carousel/assets/canberra.jpg","http://uekwww.uek.krakow.pl/files/common/uczelnia/rus/2013/1.JPG","https://upload.wikimedia.org/wikipedia/commons/f/f8/Krakow_univesity_of_economics_main_building.JPG","http://bi.gazeta.pl/im/32/1d/f8/z16260402Q,Maciej-Wilusz.jpg"];
    this.carouselBanner = {
      grid: { xs: 1, sm: 1, md: 1, lg: 1, all: 0 },
      slide: 1,
      speed: 400,
      interval: 4000,
      point: {
        visible: true,
        //not working in scss file
        pointStyles: ` 
          .ngucarouselPoint {
            list-style-type: none;
            text-align: center;
            padding: 12px;
            margin: 0;
            white-space: nowrap;
            overflow: auto;
            position: absolute;
            width: 100%;
            bottom: 20px;
            left: 0;
            box-sizing: border-box;
          }
          .ngucarouselPoint li {
            display: inline-block;
            border-radius: 999px;
            background: rgba(255, 255, 255, 0.55);
            padding: 5px;
            margin: 0 3px;
            transition: .4s ease all;
          }
          .ngucarouselPoint li.active {
              background: white;
              width: 10px;
          }
        `
      },
      load: 2,
      loop: true,
      touch: true
    };
  }

  /* It will be triggered on every slide*/
  onmoveFn(data: NguCarouselStore) {

  }

}
