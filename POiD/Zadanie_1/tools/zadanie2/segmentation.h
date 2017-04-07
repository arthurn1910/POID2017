#ifndef Segmentation_H
#define Segmentation_H

#include <QWidget>
#include <QLabel>
#include <vector>
#include "../tool.h"

namespace Ui {
class Segmentation;
struct Region;
}

struct Region
{
    bool valid;
    int regionId;
    int positionX;
    int positionY;
    int width;
    int height;
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
    int threshold;
    int maxSplitDepth;
    double calculateMeanForRegion(const double *imageData, Region region);
    double calculateDeviationForRegion(const double *imageData, Region region);
    void split(double *imageData, std::vector<Region *> *regions, Region *startingRegion, int depth = 1);


    QImage *processedImage;
    QImage *inputImage;
    Ui::Segmentation *ui;
};

#endif // Segmentation_H
