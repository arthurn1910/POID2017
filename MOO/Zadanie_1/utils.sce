function [valid, errorStringMatrix]=assertInputData(stringEquation, rangeMin, rangeMax, useMethodBisection, useMethodGoldenSection, maxIterations, precision)
    valid = %t;
    errorStringMatrix = [];
    
    containsX = strindex(stringEquation, 'x');
    containsOtherThenX = strindex(stringEquation, ['a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','y','z']);
    
    // Assertion of 'stringEquation'
    if (isempty(containsX)) then
        valid = %f;
        errorStringMatrix = [errorStringMatrix, "Funkcja nie zawiera zmiennej X."];
    end
    
    if (~isempty(containsOtherThenX)) then
        valid = %f;
        errorStringMatrix = [errorStringMatrix, "Funkcja zawiera zmienne inne niż X."];
    end
    
    // Assertion of 'rangeMin'
    if isnan(rangeMin) then
        valid = %f;
        errorStringMatrix = [errorStringMatrix, "Kraniec dolnego przedziału nie jest liczbą."];
    end
    
    // Assertion of 'rangeMax'
    if isnan(rangeMax) then
        valid = %f;
        errorStringMatrix = [errorStringMatrix, "Kraniec górnego przedziału nie jest liczbą."];
    end
    
    // Assertion of 'rangeMin' and'rangeMax'
    if ~isnan(rangeMin) & ~isnan(rangeMax) & rangeMin > rangeMax then
        valid = %f;
        errorStringMatrix = [errorStringMatrix, "Lewy kraniec przedziały jest większy od prawego."]; 
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
    
    // Assertion of 'useMethodBisection' and 'useMethodGoldenSection'
    if useMethodBisection == 0 & useMethodGoldenSection == 0 then
        valid = %f;
        errorStringMatrix = [errorStringMatrix, "Nie wybrano metody obliczeń."];         
    end
endfunction
