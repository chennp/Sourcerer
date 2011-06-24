/* 
 * Sourcerer: an infrastructure for large-scale source code analysis.
 * Copyright (C) by contributors. See CONTRIBUTORS.txt for full list.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package edu.uci.ics.sourcerer.repo.internal.core;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.TreeMap;

import edu.uci.ics.sourcerer.util.Helper;

/**
 * @author Joel Ossher (jossher@uci.edu)
 */
final class BatchSet <Project extends RepoProject> extends AbstractCollection<Project> {
  private AbstractRepository<Project> repo;
  
  private TreeMap<Integer, Batch<Project>> batches;
  private int size;
  
  protected BatchSet(AbstractRepository<Project> repo) {
    this.repo = repo;
    batches = Helper.newTreeMap();
    size = 0;
  }
  
  protected Project add(Integer batch, Integer checkout) {
    Batch<Project> b = batches.get(batch);
    if (b == null) {
      RepoFile dir = repo.repoRoot.getChild(batch.toString());
      b = new Batch<Project>(repo, dir, batch);
      batches.put(batch, b);
    }
    size++;
    return b.add(checkout);
  }
  
  protected Batch<Project> createBatch() {
    Integer batch = null;
    if (batches.isEmpty()) {
      batch = Integer.valueOf(0);
    } else {
      batch = batches.lastKey() + 1;
    }
    Batch<Project> b = new Batch<Project>(repo, repo.repoRoot.getChild(batch.toString()), batch);
    batches.put(batch, b);
    return b;
  }
  
  protected Batch<Project> getBatch(Integer batch) {
    return batches.get(batch);
  }
  
  protected Collection<Batch<Project>> getBatches() {
    return batches.values();
  }
  
  @Override
  public Iterator<Project> iterator() {
    return new Iterator<Project>() {
      Iterator<Batch<Project>> batchIter = batches.values().iterator();
      Iterator<Project> projectIter = null;
      Project next = null;
      
      @Override
      public boolean hasNext() {
        while (next == null) {
          if (projectIter == null) {
            if (batchIter == null) {
              return false;
            } else if (batchIter.hasNext()) {
              projectIter = batchIter.next().getProjects().iterator();
            } else {
              batchIter = null;
              return false;
            }
          } else if (projectIter.hasNext()) {
            next = projectIter.next();
          } else {
            projectIter = null;
          }
        }
        return true;
      }

      @Override
      public Project next() {
        if (hasNext()) {
          Project retval = next;
          next = null;
          return retval;
        } else {
          throw new NoSuchElementException();
        }
      }

      @Override
      public void remove() {
        throw new UnsupportedOperationException();
      }
    };
  }

  @Override
  public int size() {
    return size;
  }
}
