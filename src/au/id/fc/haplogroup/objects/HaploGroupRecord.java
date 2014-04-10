/*

    MIT License
    -----------

    Copyright (c) 2012 Felix Jeyareuben    

    Permission is hereby granted, free of charge, to any person obtaining a copy of this
    software and associated documentation files (the "Software"), to deal in the Software
    without restriction, including without limitation the rights to use, copy, modify,
    merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
    permit persons to whom the Software is furnished to do so, subject to the following
    conditions:

    The above copyright notice and this permission notice shall be included in all copies
    or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
    INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
    PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
    LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT
    OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
    OTHER DEALINGS IN THE SOFTWARE.
    
    Website: http://projects.fc.id.au
    
 */
package au.id.fc.haplogroup.objects;

import java.util.ArrayList;

public class HaploGroupRecord
{
    Integer id;
    String population;
    String language;
    String sampleN;
    ArrayList<RecordValue> haploGroup;
    String reference;
    String maxHaploGroup;
    double maxPercentAvailable=0; 
    
    public Integer getId()
    {
	return id;
    }

    public void setId(Integer id)
    {
	this.id = id;
    }

    public String getPopulation()
    {
	return population;
    }

    public void setPopulation(String population)
    {
	this.population = population;
    }

    public String getLanguage()
    {
	return language;
    }

    public void setLanguage(String language)
    {
	this.language = language;
    }

    public String getSampleN()
    {
	return sampleN;
    }

    public void setSampleN(String sampleN)
    {
	this.sampleN = sampleN;
    }

    public ArrayList<RecordValue> getHaploGroup()
    {
	return haploGroup;
    }

    public void setHaploGroup(ArrayList<RecordValue> haploGroup)
    {
	this.haploGroup = haploGroup;
	for(RecordValue rv:haploGroup)
	{
	    if(this.maxPercentAvailable <rv.percentAvailable)
	    {
		this.maxPercentAvailable=rv.percentAvailable;
		this.maxHaploGroup=rv.haploGroup;
	    }
	}
    }

    public String getReference()
    {
	return reference;
    }

    public void setReference(String reference)
    {
	this.reference = reference;
    }

    public String getMaxHaploGroup()
    {
        return maxHaploGroup;
    }

    public void setMaxHaploGroup(String maxHaploGroup)
    {
        this.maxHaploGroup = maxHaploGroup;
    }

    public double getMaxPercentAvailable()
    {
        return maxPercentAvailable;
    }

    public void setMaxPercentAvailable(double maxPercentAvailable)
    {
        this.maxPercentAvailable = maxPercentAvailable;
    }
    

}
