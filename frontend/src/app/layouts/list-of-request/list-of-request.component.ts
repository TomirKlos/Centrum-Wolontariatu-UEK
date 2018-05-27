import { AfterViewInit, Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { SearchService } from '../../shared/search-service.service'

import { VolunteerRequestVM, Page } from '../../shared/interfaces';
import { NguCarousel, NguCarouselStore } from '@ngu/carousel';
import { Subject } from 'rxjs/Subject';
import { MatAutocompleteModule, MatPaginator, PageEvent, MatSort } from '@angular/material';

import { RequestDialogService } from '../../requests/shared/request-dialog.service';

import { RequestService } from '../../requests/shared/request.service';
import { ServerDataSource } from '../../shared/server-data-source';


@Component({
  selector: 'app-list-of-request',
  templateUrl: './list-of-request.component.html',
  styleUrls: [ './list-of-request.component.scss' ]
})
export class ListOfRequestComponent implements OnInit, AfterViewInit {
  pathToStaticContent = "http://localhost:8080/static/";
  staticNotFoundImage = "http://localhost:8080/static/brak-obrazka.jpg"


  results: Object;
  searchTerm$ = new Subject<string>();
  searchValue: string;

  //paginator
  length = 50;
  pageIndex = 0;
  pageSize = 5;

  pageEvent: PageEvent;

  //data source
  dataSource: ServerDataSource<VolunteerRequestVM>;


  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild('searchName') input:ElementRef;

  constructor(private searchService: SearchService, private _dialogService: RequestDialogService, private _requestService: RequestService) {
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
  this.paginator.pageSize=this.pageSize;
  this.dataSource = new ServerDataSource<VolunteerRequestVM>(this._requestService, this.paginator, new MatSort, "volunteerRequestAcceptedOnly");
  this.dataSource.relativePathToServerResource = '';
  this.dataSource.loadAcceptedVrPage();

    this.carouselBanerItems = ["https://sheikalthaf.github.io/ngx-carousel/assets/canberra.jpg","http://uekwww.uek.krakow.pl/files/common/uczelnia/rus/2013/1.JPG","https://upload.wikimedia.org/wikipedia/commons/f/f8/Krakow_univesity_of_economics_main_building.JPG"];
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

  ngAfterViewInit() {
    this.dataSource.initAfterViewInit();
  }

  /* It will be triggered on every slide*/
  onmoveFn(data: NguCarouselStore) {

  }

  searchContent(){
    if(this.searchValue=="")
      this.dataSource.loadAcceptedVrPage();
    else if(this.searchValue!=""){
      this.dataSource.generateFilteredSearchPage(this.searchTerm$);
      this.results=null;
    }
      
  }

  openDialog(element: VolunteerRequestVM): void {
    this._dialogService.open(element);
  }

  onClickResetSearchButton(){
    this.dataSource.loadAcceptedVrPage();
  }

}

