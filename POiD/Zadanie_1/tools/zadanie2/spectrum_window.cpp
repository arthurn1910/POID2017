#include "spectrum_window.h"
#include "ui_spectrum_window.h"

SpectrumWindow::SpectrumWindow(QWidget *parent) :
    QMainWindow(parent),
    ui(new Ui::SpectrumWindow)
{
    ui->setupUi(this);
    setupQGraphicsViews();
}

SpectrumWindow::~SpectrumWindow()
{
    delete ui;
}


void SpectrumWindow::setupQGraphicsViews()
{
    originalGraphicView = ui->originalSpectrum;
    originalGrahpicScane = new QGraphicsScene();
    originalGrahpicPixmap = new QGraphicsPixmapItem();
    originalGrahpicScane->addItem(originalGrahpicPixmap);
    originalGraphicView->setScene(originalGrahpicScane);
    originalGraphicView->show();

    withMaskGraphicView = ui->withMaskSpectrum;
    withMaskGrahpicScane = new QGraphicsScene();
    withMaskGrahpicPixmap = new QGraphicsPixmapItem();
    withMaskGrahpicScane->addItem(withMaskGrahpicPixmap);
    withMaskGraphicView->setScene(withMaskGrahpicScane);
    withMaskGraphicView->show();
}

void SpectrumWindow::setOriginalSpectrum(QImage *image) {
    originalGrahpicImage = image;
    originalGrahpicPixmap->setPixmap(QPixmap::fromImage(*originalGrahpicImage));
}

void SpectrumWindow::setWithMaskSpectrum(QImage *image) {
    withMaskGrahpicImage = image;
    withMaskGrahpicPixmap->setPixmap(QPixmap::fromImage(*withMaskGrahpicImage));
}
