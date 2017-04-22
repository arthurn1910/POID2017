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

//        Segment segment;
//        Region firstRegion;
//        firstRegion.regionId = 100;
//        firstRegion.positionX = 100;
//        firstRegion.positionY = 100;
//        firstRegion.height = 100;
//        firstRegion.width = 100;
//        segment.addRegion(&firstRegion);
//        qDebug() << "Size: " << segment.size;
//        qDebug() << "Mean: " << segment.mean;

//        Region secondRegion;
//        secondRegion.regionId = 100;
//        secondRegion.positionX = 200;
//        secondRegion.positionY = 100;
//        secondRegion.height = 100;
//        secondRegion.width = 100;

//        qDebug() << "Can add: " << segment.canAddRegion(&secondRegion, threshold);

        qDebug() << "Regions: " << regions->size() << "\n";
        if (regions->size() > 0) {
            marge(regions);

            int color = 255 / segments->size();

            for (int k = 0; k < segments->size(); k++) {
                Segment *tempSegment = segments->at(k);
//                if (tempSegment->regions.size() < 5) {
//                    continue;
//                }
//                qDebug() << "Segment: " << k << ", Region: " << tempSegment->regions.size();
                for (int m = 0; m < tempSegment->regions.size(); m++) {
                    Region *temp = tempSegment->regions.at(m);
//                    qDebug() << "Segment: " << k << ", Region: " << m << ", X: " << temp->positionX << "\n";
//                    qDebug() << "Segment: " << k << ", Region: " << m << ", Y: " << temp->positionY << "\n";
//                    qDebug() << "Segment: " << k << ", Region: " << m << ", width: " << temp->width << "\n";
//                    qDebug() << "Segment: " << k << ", Region: " << m << ", height: " << temp->height << "\n";
                    for (int i = 0; i < temp->height; i++) {
                        for (int j = 0; j < temp->width; j++) {
//                            if (i == 0 || i == temp->height - 1) {
                                imageData[(temp->positionY + i) * WIDTH + temp->positionX + j] = tempSegment->mean;
//                            }
                        }
//                        imageData[(temp->positionY + i) * WIDTH + temp->positionX] = tempSegment->mean;
//                        imageData[(temp->positionY + i) * WIDTH + temp->positionX + temp->width - 1] = tempSegment->mean;
                    }
                }
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

    return (mean / (region.height * region.width));
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
    return std::sqrt(deviation / (region.height * region.width));
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

void Segmentation::marge(std::vector<Region *> *regions) {
    if (segments != nullptr) {
        for (int i = 0; i < segments->size(); i++) {
            delete segments->at(i);
        }
        delete segments;
    }

    segments = new std::vector<Segment *>;
    bool addedNewRegion = true;
    bool addedNewSegment = true;
    Segment *currentSegment = new Segment();

    while (addedNewSegment) {
        addedNewSegment = false;
        addedNewRegion = true;
        currentSegment->addRegion(regions->at(0));
        regions->erase(regions->begin());

        while (addedNewRegion) {
            addedNewRegion = false;

            for (int i = 0; i < regions->size(); i++) {
                if (currentSegment->canAddRegion(regions->at(i), threshold)) {
                    addedNewRegion = true;
                    currentSegment->addRegion(regions->at(i));
                    regions->erase(regions->begin() + i);
                    i--;
                }
            }
        }

        if (currentSegment->regions.size() > 0) {
            segments->push_back(currentSegment);
            if (regions->size() > 0) {
                addedNewSegment = true;
                currentSegment = new Segment();
            }
        }
    }
    bool margedSegment = true;

    qDebug() << "Segments before marge: " << segments->size();
    while (margedSegment) {
        margedSegment = false;
        if (segments->size() < 2)
            break;
        for (int i = 0; i < segments->size(); i++) {
            //qDebug() << "Sprawdzam segment: " << i;
            Segment *currentSegment = segments->at(i);
            for (int j = i + 1; j < segments->size(); j++) {
                //qDebug() << "Porownuje z segmentem: " << j << "/" << segments->size();
                Segment *tempSegment = segments->at(j);
                if (currentSegment->canMargeSegments(tempSegment, threshold)) {
                    //qDebug() << "Lacze z segmentem: " << j << "/" << segments->size();
                    currentSegment->margeSegments(tempSegment);
                    margedSegment = true;
                    delete tempSegment;
                    segments->erase(segments->begin() + j);
                    j--;
                }
            }
        }
    }
    qDebug() << "Segments after marge: " << segments->size();
}

Segment::Segment() {

}

Segment::~Segment() {

}

void Segment::addRegion(Region *region) {
    regions.push_back(region);
    size += (region->height * region->width);
    calculateNewMean();
}

void Segment::calculateNewMean() {
    mean = 0;
    for (int i = 0; i < regions.size(); i++) {
        Region *currentRegion = regions.at(i);
        mean += currentRegion->regionId * (currentRegion->height * currentRegion->width);
    }

    mean /= size;
}

bool Segment::canAddRegion(const Region *region, double threshold) {
    if (std::abs(mean - region->regionId) > threshold) {
        return false;
    }

    for (int i = 0; i < regions.size(); i++) {
        Region *currentRegion = regions.at(i);

        // ABOVE
        if (currentRegion->positionY == region->positionY + region->height) {
            if (currentRegion->positionX <= region->positionX && currentRegion->positionX + currentRegion->width >= region->positionX + region->width) {
                return true;
            } else if (currentRegion->positionX >= region->positionX && currentRegion->positionX + currentRegion->width <= region->positionX + region->width) {
                return true;
            }
        }
        // BELOW
        if (currentRegion->positionY + currentRegion->height == region->positionY) {
            if (currentRegion->positionX <= region->positionX && currentRegion->positionX + currentRegion->width >= region->positionX + region->width) {
                return true;
            } else if (currentRegion->positionX >= region->positionX && currentRegion->positionX + currentRegion->width <= region->positionX + region->width) {
                return true;
            }
        }
        // RIGHT
        if (currentRegion->positionX + currentRegion->width == region->positionX) {
            if (currentRegion->positionY <= region->positionY && currentRegion->positionY + currentRegion->height >= region->positionY + region->height) {
                return true;
            } else if (currentRegion->positionY >= region->positionY && currentRegion->positionY + currentRegion->height <= region->positionY + region->height) {
                return true;
            }
        }
        // LEFT
        if (currentRegion->positionX == region->positionX + region->width) {
            if (currentRegion->positionY <= region->positionY && currentRegion->positionY + currentRegion->height >= region->positionY + region->height) {
                return true;
            } else if (currentRegion->positionY >= region->positionY && currentRegion->positionY + currentRegion->height <= region->positionY + region->height) {
                return true;
            }
        }
    }

    return false;
}

bool Segment::canMargeSegments(const Segment *segment, double threashold) {
    if (std::abs(mean - segment->mean) > threashold) {
        return false;
    }
    //qDebug() << "Sprawdzilem srednia";
    for (int i = 0; i < regions.size(); i++) {
        //qDebug() << "Biore moj region: " << i << "/" << regions.size();
        Region *currentRegion = regions.at(i);
        //qDebug() << "Wzialem moj region: " << i << "/" << regions.size();
        for (int j = 0; j < segment->regions.size(); j++) {
            //qDebug() << "Biore obcy region: " << j << "/" << segment->regions.size();
            Region *tempRegion = segment->regions.at(j);
            //qDebug() << "Wzialem obcy region: " << j << "/" << segment->regions.size();
            // ABOVE
            if (currentRegion->positionY == tempRegion->positionY + tempRegion->height) {
                if (currentRegion->positionX <= tempRegion->positionX && currentRegion->positionX + currentRegion->width >= tempRegion->positionX + tempRegion->width) {
                    //qDebug() << "ABOVE";
                    return true;
                } else if (currentRegion->positionX >= tempRegion->positionX && currentRegion->positionX + currentRegion->width <= tempRegion->positionX + tempRegion->width) {
                    //qDebug() << "ABOVE";
                    return true;
                }
            }
            // BELOW
            if (currentRegion->positionY + currentRegion->height == tempRegion->positionY) {
                if (currentRegion->positionX <= tempRegion->positionX && currentRegion->positionX + currentRegion->width >= tempRegion->positionX + tempRegion->width) {
                    //qDebug() << "BELOW";
                    return true;
                } else if (currentRegion->positionX >= tempRegion->positionX && currentRegion->positionX + currentRegion->width <= tempRegion->positionX + tempRegion->width) {
                    //qDebug() << "BELOW";
                    return true;
                }
            }
            // RIGHT
            if (currentRegion->positionX + currentRegion->width == tempRegion->positionX) {
                if (currentRegion->positionY <= tempRegion->positionY && currentRegion->positionY + currentRegion->height >= tempRegion->positionY + tempRegion->height) {
                    //qDebug() << "RIGHT";
                    return true;
                } else if (currentRegion->positionY >= tempRegion->positionY && currentRegion->positionY + currentRegion->height <= tempRegion->positionY + tempRegion->height) {
                    //qDebug() << "RIGHT";
                    return true;
                }
            }
            // LEFT
            if (currentRegion->positionX == tempRegion->positionX + tempRegion->width) {
                if (currentRegion->positionY <= tempRegion->positionY && currentRegion->positionY + currentRegion->height >= tempRegion->positionY + tempRegion->height) {
                    //qDebug() << "LEFT";
                    return true;
                } else if (currentRegion->positionY >= tempRegion->positionY && currentRegion->positionY + currentRegion->height <= tempRegion->positionY + tempRegion->height) {
                    //qDebug() << "LEFT";
                    return true;
                }
            }
        }
    }
    return false;
}

void Segment::margeSegments(Segment *segment) {
    for (int i = 0; i < segment->regions.size(); i++) {
        Region *tempRegion = segment->regions.at(i);
        this->addRegion(tempRegion);
    }
}
