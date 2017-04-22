#ifndef Segmentation_H
#define Segmentation_H

#include <QWidget>
#include <QLabel>
#include <vector>
#include "../tool.h"

namespace Ui {
class Segmentation;
struct Region;
class Segment;
}

struct Region
{
    int regionId;
    int positionX;
    int positionY;
    int width;
    int height;
};

class Segment {
public:
    Segment();
    ~Segment();

    std::vector<Region *> regions;
    double mean;
    double size = 0;

    bool canMargeSegments(const Segment *segment, double threashold);
    void margeSegments(Segment *segment);

    bool canAddRegion(const Region *region, double threshold);
    void addRegion(Region *region);
    void calculateNewMean();
};

class Segmentation : public Tool
{
    Q_OBJECT
public:
    explicit Segmentation(QWidget *parent = 0);
    explicit Segmentation(int width, int height, int depth, QWidget *parent = 0);
    ~Segmentation();

    QImage* process(QImage *photoData);

private:
    double threshold;
    int maxSplitDepth;
    double calculateMeanForRegion(const double *imageData, Region region);
    double calculateDeviationForRegion(const double *imageData, Region region);
    void split(double *imageData, std::vector<Region *> *regions, Region *startingRegion, int depth = 1);
    void marge(std::vector<Region *> *regions);

    std::vector<Segment *> *segments = nullptr;
    QImage *processedImage;
    QImage *inputImage;
    Ui::Segmentation *ui;
};

#endif // Segmentation_H
