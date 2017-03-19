
function [y, currentIteration, currentRange]=goldenSection(equation, rangeMin, rangeMax, maxIterations, minRange)
    currentRangeMin=rangeMin;
    currentRangeMax=rangeMax;
    currentIteration=1;
    k=(sqrt(5)-1)/2;
    
    x1 = currentRangeMax - k*(currentRangeMax - currentRangeMin);
    x2 = currentRangeMin + k*(currentRangeMax - currentRangeMin);

    while currentIteration <= maxIterations
        if((currentRangeMax -currentRangeMin)>precision) then
            x = x1;
            f1 = evstr(equation);
            x = x2;
            f2 = evstr(equation);
            
            if(f2 < f1) then
                currentRangeMin = x1;
                x1=x2;
                x2=currentRangeMin + k*(currentRangeMax - currentRangeMin);
            else
                currentRangeMax = x2;
                x2=x1;
                x1=currentRangeMax - k*(currentRangeMax - currentRangeMin);
            end
            y=(currentRangeMin+currentRangeMax)/2;
            disp('iter=');
            disp(currentIteration);
        end
        currentIteration=currentIteration+1;
    end
    disp(currentRangeMin);
    disp(currentRangeMax);
    disp('y');
    disp(y);
endfunction
