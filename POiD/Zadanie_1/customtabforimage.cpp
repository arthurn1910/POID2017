#include "customtabforimage.h"
#include "ui_customtabforimage.h"
#include "tools/zadanie1/brightness.h"
#include "tools/zadanie1/contrast.h"
#include "tools/zadanie1/negative.h"
#include "tools/zadanie1/filtr_mean.h"
#include "tools/zadanie1/filtr_median.h"
#include "tools/zadanie1/histogram.h"
#include "tools/zadanie1/filtr_linear_splot.h"
#include "tools/zadanie1/filtr_sobel.h"
#include "tools/zadanie2/low_high_pass_filter.h"
#include "tools/zadanie2/band_pass_stop_filter.h"
#include "tools/zadanie2/edge_detection_filter.h"
#include "tools/zadanie2/phase_filter.h"
#include "tools/zadanie2/segmentation.h"
#include <QGraphicsView>
#include <QMessageBox>
#include <QImageReader>
#include <QImageWriter>
#include <QDir>
#include <QGraphicsPixmapItem>
#include <string>
#include <QFileDialog>
#include <QStandardPaths>

CustomTabForImage::CustomTabForImage(QWidget *parent) :
    QWidget(parent),
    ui(new Ui::CustomTabForImage)
{
    ui->setupUi(this);
    tool = nullptr;
}

CustomTabForImage::CustomTabForImage(QWidget *parent, QString graphicFilePath) :
    QWidget(parent),
    ui(new Ui::CustomTabForImage)
{
    tool = nullptr;
    ui->setupUi(this);

    toolLayout = ui->toolLayout;
    this->graphicFilePath = graphicFilePath;

    setupQGraphicsViews();
    loadFile();
}

CustomTabForImage::~CustomTabForImage()
{
    delete ui;
}

void CustomTabForImage::setupQGraphicsViews()
{
    originalGraphicView = ui->originalGrahpic;
    originalGrahpicScane = new QGraphicsScene();
    originalGrahpicPixmap = new QGraphicsPixmapItem();
    originalGrahpicScane->addItem(originalGrahpicPixmap);
    originalGraphicView->setScene(originalGrahpicScane);
    originalGraphicView->show();

    processedGraphicView = ui->processedGraphic;
    processedGrahpicScane = new QGraphicsScene();
    processedGrahpicPixmap = new QGraphicsPixmapItem();
    processedGrahpicScane->addItem(processedGrahpicPixmap);
    processedGraphicView->setScene(processedGrahpicScane);
    processedGraphicView->show();
}

void CustomTabForImage::loadFile() {
    originalGrahpicImage = new QImage(graphicFilePath);
    originalGrahpicPixmap->setPixmap(QPixmap::fromImage(*originalGrahpicImage));
}

void CustomTabForImage::on_toolsComboBox_currentIndexChanged(int index)
{
    cleanToolLayout();
    if (!tool) {
        delete tool;
        tool = nullptr;
    }
    if (index == 0) {
        ui->transformButton->setEnabled(false);
    } else {
        tool = createTool(index);

        toolLayout->addWidget((QWidget *)tool);
        ui->transformButton->setEnabled(true);
    }
}

void CustomTabForImage::cleanToolLayout()
{
    QLayoutItem *item;
    while ( ( item = toolLayout->takeAt( 0 ) ) != NULL )
    {
        delete item->widget();
        delete item;
    }
}

Tool *CustomTabForImage::createTool(int index)
{
    switch (index) {
    case 1:
        return new Brightness(originalGrahpicImage->width(),
                              originalGrahpicImage->height(),
                              originalGrahpicImage->depth(),
                              this);
        break;
    case 2:
        return new Contrast(originalGrahpicImage->width(),
                              originalGrahpicImage->height(),
                              originalGrahpicImage->depth(),
                              this);
        break;
    case 3:
        return new Negative(originalGrahpicImage->width(),
                              originalGrahpicImage->height(),
                              originalGrahpicImage->depth(),
                              this);
        break;
    case 4:
        return new FiltrMean(originalGrahpicImage->width(),
                              originalGrahpicImage->height(),
                              originalGrahpicImage->depth(),
                              this);
    case 5:
        return new FiltrMedian(originalGrahpicImage->width(),
                              originalGrahpicImage->height(),
                              originalGrahpicImage->depth(),
                              this);
    case 6:
        return new Histogram(originalGrahpicImage->width(),
                              originalGrahpicImage->height(),
                              originalGrahpicImage->depth(),
                              this);
    case 7:
        return new FiltrLinearSplot(originalGrahpicImage->width(),
                              originalGrahpicImage->height(),
                              originalGrahpicImage->depth(),
                              this);
    case 8:
        return new FiltrSobel(originalGrahpicImage->width(),
                              originalGrahpicImage->height(),
                              originalGrahpicImage->depth(),
                              this);
        break;
    case 9:
        return new LowHighPassFilter(originalGrahpicImage->width(),
                              originalGrahpicImage->height(),
                              originalGrahpicImage->depth(),
                              this);
    case 10:
        return new BandPassStopFilter(originalGrahpicImage->width(),
                              originalGrahpicImage->height(),
                              originalGrahpicImage->depth(),
                              this);
    case 11:
        return new EdgeDetectionFilter(originalGrahpicImage->width(),
                              originalGrahpicImage->height(),
                              originalGrahpicImage->depth(),
                              this);
        break;
    case 12:
        return new PhaseFilter(originalGrahpicImage->width(),
                              originalGrahpicImage->height(),
                              originalGrahpicImage->depth(),
                              this);
        break;
    case 13:
        return new Segmentation(originalGrahpicImage->width(),
                              originalGrahpicImage->height(),
                              originalGrahpicImage->depth(),
                              this);
        break;
    default:
        throw new std::exception();
    }
}

void CustomTabForImage::on_transformButton_clicked()
{
    if (tool == nullptr)
        return;
    processedGrahpicImage = tool->process(originalGrahpicImage);

    processedGrahpicPixmap->setPixmap(QPixmap::fromImage(*processedGrahpicImage));
    processedGraphicView->repaint();
}

QImage *CustomTabForImage::getProcessedQImage()
{
    return processedGrahpicImage;
}

void CustomTabForImage::on_restoreButton_clicked()
{

}

void CustomTabForImage::on_swapButton_clicked()
{

}
