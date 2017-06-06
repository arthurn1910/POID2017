function [valid, errorStringMatrix]=assertInputData(stringEquation, rangeXMin, rangeXMax,rangeYMin, rangeYMax, maxIterations, precision)
    valid = %t;
    errorStringMatrix = [];
    
    containsX = strindex(stringEquation, 'x');
    containsY = strindex(stringEquation, 'y');
    containsOtherThenX = strindex(stringEquation, ['a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','z']);
    
    // Assertion of 'stringEquation'
    if (isempty(containsX)) then
        valid = %f;
        errorStringMatrix = [errorStringMatrix, "Funkcja nie zawiera zmiennej X."];
    end
    
    if (isempty(containsY)) then
        valid = %f;
        errorStringMatrix = [errorStringMatrix, "Funkcja nie zawiera zmiennej Y."];
    end
    
    if (~isempty(containsOtherThenX)) then
        valid = %f;
        errorStringMatrix = [errorStringMatrix, "Funkcja zawiera zmienne inne niż X,Y."];
    end
    
    // Assertion of 'rangeMin'
    if isnan(rangeXMin) then
        valid = %f;
        errorStringMatrix = [errorStringMatrix, "Kraniec dolnego przedziału X nie jest liczbą."];
    end
    
    // Assertion of 'rangeMax'
    if isnan(rangeXMax) then
        valid = %f;
        print('%f',rangeXMax);
        errorStringMatrix = [errorStringMatrix, "Kraniec górnego przedziału X nie jest liczbą."];
    end
    
    // Assertion of 'rangeMin' and'rangeMax'
    if ~isnan(rangeXMin) & ~isnan(rangeXMax) & rangeXMin > rangeXMax then
        valid = %f;
        errorStringMatrix = [errorStringMatrix, "Lewy kraniec przedziału X jest większy od prawego."]; 
    end   
    
    // Assertion of 'rangeMin'
    if isnan(rangeYMin) then
        valid = %f;
        errorStringMatrix = [errorStringMatrix, "Kraniec dolnego przedziału Y nie jest liczbą."];
    end
    
    // Assertion of 'rangeMax'
    if isnan(rangeYMax) then
        valid = %f;
        errorStringMatrix = [errorStringMatrix, "Kraniec górnego przedziału Y nie jest liczbą."];
    end
    
    // Assertion of 'rangeMin' and'rangeMax'
    if ~isnan(rangeYMin) & ~isnan(rangeYMax) & rangeYMin > rangeYMax then
        valid = %f;
        errorStringMatrix = [errorStringMatrix, "Lewy kraniec przedziału Y jest większy od prawego."]; 
    end   
    
    // Assertion of 'maxIterations'
    if isnan(maxIterations) then
        valid = %f;
        errorStringMatrix = [errorStringMatrix, "Liczba maksymalnych iteracji nie jest liczbą"]; 
    end
        
    if ~isnan(maxIterations) & maxIterations <= 0 then
        valid = %f;
        errorStringMatrix = [errorStringMatrix, "Liczba maksymalnych iteracji jest mniejsza lub równa zero."]; 
    end
    
    // Assertion of 'precision'
    if isnan(precision) then
        valid = %f;
        errorStringMatrix = [errorStringMatrix, "Wartość dokłądności nie jest liczbą"]; 
    end
    
    if ~isnan(precision) & precision < 0 then
        valid = %f;
        errorStringMatrix = [errorStringMatrix, "Wartość dokłądności jest mniejsza od zero."]; 
    end
    
endfunction



function [xs, ys]=find3thTopyui(x1, y1, x2, y2, distance,equation)
      xd=(x1+x2)/2;
      yd=(y1+y2)/2;
      a=(y2-y1)/(x2-x1);
      b=y1-((y2-y1)/(x2-x1))*x1;
      
      a1=-1/a;
      b1=yd-a1*xd;
      
      yk=a1*xd+b1;
      delta=sqrt((2*(a1*b1-a1*y2-x2))^2-4*(1+a1^2)*(b1^2-2*b1*y2-distance^2+x2^2+y2^2));
           
      xs=((-1)*(2*a1*b1-2*a1*y2-2*x2)-delta*5/4)/2*(1+a1^2);
      ys=a1*xs+b1;
            
      vDistanceTop1=sqrt((xp1-xp2)^2+(yp1-yp2)^2)
      vDistanceTop2=sqrt((xs-xp2)^2+(ys-yp2)^2)
      vDistanceTop3=sqrt((xp1-xs)^2+(yp1-ys)^2)
      mprintf("\n %f, %f, %f \n",vDistanceTop1,vDistanceTop2,vDistanceTop3)
endfunction
