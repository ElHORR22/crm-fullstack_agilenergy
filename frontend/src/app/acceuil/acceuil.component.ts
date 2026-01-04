import { CommonModule } from '@angular/common';
import { AfterViewInit, Component, ElementRef, HostListener, QueryList, ViewChildren } from '@angular/core';

@Component({
  selector: 'app-acceuil',
  imports: [CommonModule],
  templateUrl: './acceuil.component.html',
  styleUrls: ['./acceuil.component.css'],
  standalone: true,
})
export class AcceuilComponent implements AfterViewInit{

  title = 'acceuil';
  
  @ViewChildren('backgroundVideo') videoRefs!: QueryList<ElementRef<HTMLVideoElement>>;

  ngAfterViewInit(): void {
    this.videoRefs.forEach(videoRef => {
      const video = videoRef.nativeElement;
      video.muted = true;
      video.setAttribute('muted', '');
      video.play().catch((e) => {
        console.warn('Autoplay bloqué :', e);
      });
    });
  }

  scrollTo(section: string) {
    const element = document.getElementById(section);
    if (element) {
      element.scrollIntoView({ behavior: 'smooth' });
    } else {
      console.error(`Section ${section} introuvable.`);
    }
  } 

  @HostListener('window:scroll', [])
  onScroll(): void {
  const sections = document.querySelectorAll('.cartella');

  sections.forEach((section) => {
    const texte = section.querySelector('.cartellatexte');
    const image = section.querySelector('.cadreimage');

    if (section) {
      const rect = section.getBoundingClientRect();
      const inView = rect.top < window.innerHeight * 0.75;

      if (inView) {
        texte?.classList.add('revealslide');
        image?.classList.add('moveimage');
      }
    }
  });
  
}
}


