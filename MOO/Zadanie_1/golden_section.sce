
function [y, currentIteration, currentRange]=goldenSection(equation, rangeMin, rangeMax, maxIterations, minRange)
    currentRangeMin=rangeMin;
    currentRangeMax=rangeMax;
    currentIteration=1;
    k=(sqrt(5)-1)/2;
    
    range=rangeMax-rangeMin;
    disp('range');
    disp(range);
    delta=range/10;
    disp(delta);
    
    for i=0:8
        u0=rangeMin+i*delta;
        u1=rangeMin+i*delta+delta;
        u2=rangeMin++i*delta+2*delta;
        x=u0;
        fu0=evstr(equation);
        x=u1;
        fu1=evstr(equation);
        x=u2;
        fu2=evstr(equation);
        if((fu0>=fu1) & (fu1<=fu2)) then
           currentRangeMin=u0;
           currentRangeMax=u2;
           break; 
        end
    end
    disp('przedzial unimodalny:');
    disp(currentRangeMin);
    disp(currentRangeMax);
    disp('?????');
    
    
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
