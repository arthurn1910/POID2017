#include "customtabforimage.h"
#include "ui_customtabforimage.h"
#include "tools/brightness.h"
#include "tools/contrast.h"
#include "tools/negative.h"
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
