#include "segmentation.h"
#include "ui_segmentation.h"
#include <QtDebug>
Segmentation::Segmentation(QWidget *parent) :
    Tool(parent),
    ui(new Ui::Segmentation)
{
    ui->setupUi(this);
}

Segmentation::Segmentation(int width, int height, int depth, QWidget *parent) :
    Tool(width, height, depth, parent),
    ui(new Ui::Segmentation)
{
    ui->setupUi(this);
}

Segmentation::~Segmentation()
{
    delete ui;
}

QImage *Segmentation::process(QImage *image)
{
    inputImage = image;

    threshold = ui->thresholdSpinBox->value();
    maxSplitDepth = ui->maxDepthSpinBox->value();

    if (DEPTH == 8) {
        uchar *imageBits = image->bits();
        uchar *processedBits = new uchar[HEIGHT * WIDTH];
        double *imageData = new double[WIDTH * HEIGHT];
        std::vector<Region *> *regions = new std::vector<Region *>;
        Region *startingRegion = new Region;
        for (int i = 0; i < WIDTH * HEIGHT; i++) {
            imageData[i] = imageBits[i];
        }
        startingRegion->height = HEIGHT;
        startingRegion->width = WIDTH;
        startingRegion->positionX = 0;
        startingRegion->positionY = 0;

        split(imageData, regions, startingRegion);

        qDebug() << "Regions: " << regions->size() << "\n";

        for (int k = 0; k < regions->size(); k++) {
            Region *temp = regions->at(k);
            for (int i = 0; i < temp->height; i++) {
                for (int j = 0; j < temp->width; j++) {
                    //if (i == 0 || i == temp->height - 1) {
                        imageData[(temp->positionY + i) * WIDTH + temp->positionX + j] = temp->regionId;
                    //}
                }
                //imageData[(temp->positionY + i) * WIDTH + temp->positionX] = 0;
                //imageData[(temp->positionY + i) * WIDTH + temp->positionX + temp->width] = 0;
            }
        }


        for (int i = 0; i < WIDTH * HEIGHT; i++) {
            processedBits[i] = (uchar) imageData[i];
        }
        processedImage = new QImage(processedBits, WIDTH, HEIGHT, image->bytesPerLine(), image->format());
        for (int i = 0; i < 256; i++) {
            processedImage->setColor(i, qRgb(i, i, i));
        }
    } else {

    }

    return processedImage;
}

double Segmentation::calculateMeanForRegion(const double *imageData, Region region)
{
    double mean = 0;
    for (int i = 0; i < region.height; i++) {
        for (int j = 0; j < region.width; j++) {
            mean += imageData[(region.positionY + i) * WIDTH + region.positionX + j];
        }
    }
    return mean / (region.height + region.width);
}

double Segmentation::calculateDeviationForRegion(const double *imageData, Region region)
{
    double mean = calculateMeanForRegion(imageData, region);
    double deviation = 0;
    for (int i = 0; i < region.height; i++) {
        for (int j = 0; j < region.width; j++) {
            deviation += std::pow(imageData[(region.positionY + i) * WIDTH + region.positionX + j] - mean, 2);
        }
    }
    return std::sqrt(deviation / (region.height + region.width));
}

void Segmentation::split(double *imageData, std::vector<Region *> *regions, Region *startingRegion, int depth)
{
    if (depth > maxSplitDepth) {
        delete startingRegion;
        return;
    } else if (startingRegion->height == 1 && startingRegion->width == 1) {
        startingRegion->regionId = imageData[startingRegion->positionY * WIDTH + startingRegion->positionX];
        regions->push_back(startingRegion);
        return;
    }

    double mean = calculateMeanForRegion(imageData, *startingRegion);
    double deviation = calculateDeviationForRegion(imageData, *startingRegion);

    if (deviation < threshold) {
        startingRegion->regionId = mean;
        regions->push_back(startingRegion);
        return;
    } else {
        int newHeight = startingRegion->height / 2;
        if (newHeight == 0) {
            newHeight = 1;
        }

        int newWidth = startingRegion->width / 2;
        if (newWidth == 0) {
            newWidth = 1;
        }

        Region *leftTop = new Region;
        leftTop->height = newHeight;
        leftTop->width = newWidth;
        leftTop->positionX = startingRegion->positionX;
        leftTop->positionY = startingRegion->positionY;
        split(imageData, regions, leftTop, ++depth);

        Region *rightTop = new Region;
        rightTop->height = newHeight;
        rightTop->width = newWidth;
        rightTop->positionX = startingRegion->positionX + newWidth;
        rightTop->positionY = startingRegion->positionY;
        split(imageData, regions, rightTop, depth);

        Region *leftBottom = new Region;
        leftBottom->height = newHeight;
        leftBottom->width = newWidth;
        leftBottom->positionX = startingRegion->positionX;
        leftBottom->positionY = startingRegion->positionY + newHeight;
        split(imageData, regions, leftBottom, depth);

        Region *rightBottom = new Region;
        rightBottom->height = newHeight;
        rightBottom->width = newWidth;
        rightBottom->positionX = startingRegion->positionX + newWidth;
        rightBottom->positionY = startingRegion->positionY + newHeight;
        split(imageData, regions, rightBottom, depth);
    }
}
